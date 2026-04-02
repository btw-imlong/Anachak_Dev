package AccomManage.system.Service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;
import AccomManage.system.Service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private AttendanceRecordRepository recordRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private TeacherRepository teacherRepo;
    @Autowired private TeacherRoomRepository teacherRoomRepo;

    // ✅ Toggle help mode on/off
    @Override
    public ToggleHelpModeResponse toggleHelpMode() {
        Teacher teacher = getLoggedInTeacher();
        teacher.setHelpMode(!teacher.isHelpMode()); // flip it
        teacherRepo.save(teacher);

        ToggleHelpModeResponse res = new ToggleHelpModeResponse();
        res.setTeacherId(teacher.getId());
        res.setTeacherName(teacher.getName());
        res.setHelpMode(teacher.isHelpMode());
        res.setMessage(teacher.isHelpMode()
                ? "Help mode ON — you can now mark attendance for any room"
                : "Help mode OFF — you can only mark attendance for your assigned rooms");
        return res;
    }

    // ✅ Create attendance session — checks room ownership
    @Override
    public void createAttendance(CreateAttendanceRequest request) {
        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        // check teacher is allowed to access this room
        Teacher teacher = getLoggedInTeacher();
        checkRoomAccess(teacher, room);

        if (attendanceRepo.existsByRoomIdAndDate(room.getId(), request.getDate()))
            throw new RuntimeException("Attendance already taken for this room on this date!");

        Attendance attendance = new Attendance();
        attendance.setRoom(room);
        attendance.setDate(request.getDate());
        attendance.setCreatedAt(LocalDateTime.now());
        attendanceRepo.save(attendance);

        List<Student> students = studentRepo.findByRoomId(room.getId());
        if (students.isEmpty()) throw new RuntimeException("No students in this room!");

        List<AttendanceRecord> records = new ArrayList<>();
        for (Student s : students) {
            AttendanceRecord r = new AttendanceRecord();
            r.setAttendance(attendance);
            r.setStudent(s);
            r.setStatus(Status.ABSENT);
            records.add(r);
        }
        recordRepo.saveAll(records);
    }

    // ✅ Get attendance — checks room ownership
    @Override
    public List<AttendanceRecordResponse> getAttendanceByRoomAndDate(String roomNumber, LocalDate date) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

        Teacher teacher = getLoggedInTeacher();
        checkRoomAccess(teacher, room);

        Attendance attendance = attendanceRepo.findByRoomIdAndDate(room.getId(), date)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        List<AttendanceRecord> records = recordRepo.findByAttendanceId(attendance.getId());
        List<AttendanceRecordResponse> response = new ArrayList<>();

        for (AttendanceRecord r : records) {
            AttendanceRecordResponse res = new AttendanceRecordResponse();
            res.setRecordId(r.getId());
            res.setStudentId(r.getStudent().getId());
            res.setStudentName(r.getStudent().getName());
            res.setStatus(r.getStatus().name());
            if (r.getTakenBy() != null) res.setTeacherName(r.getTakenBy().getName());
            response.add(res);
        }
        return response;
    }

    // ✅ Bulk update — checks room ownership via record
    @Override
    public void updateBulkAttendance(BulkAttendanceUpdateRequest request) {
        Teacher teacher = getLoggedInTeacher();
        List<AttendanceRecord> records = new ArrayList<>();

        for (BulkAttendanceUpdateRequest.RecordUpdate r : request.getRecords()) {
            AttendanceRecord record = recordRepo.findById(r.getRecordId())
                    .orElseThrow(() -> new RuntimeException("Record not found: " + r.getRecordId()));

            // check room access via the attendance session
            checkRoomAccess(teacher, record.getAttendance().getRoom());

            try {
                record.setStatus(Status.valueOf(r.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + r.getStatus() + ". Use PRESENT, ABSENT, or LATE");
            }
            record.setTakenBy(teacher);
            records.add(record);
        }
        recordRepo.saveAll(records);
    }

    // ✅ Update single record — checks room ownership
    @Override
    public AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request) {
        AttendanceRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        Teacher teacher = getLoggedInTeacher();
        checkRoomAccess(teacher, record.getAttendance().getRoom());

        try {
            record.setStatus(Status.valueOf(request.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + request.getStatus() + ". Use PRESENT, ABSENT, or LATE");
        }
        record.setTakenBy(teacher);

        AttendanceRecord updated = recordRepo.save(record);

        AttendanceRecordResponse res = new AttendanceRecordResponse();
        res.setRecordId(updated.getId());
        res.setStudentId(updated.getStudent().getId());
        res.setStudentName(updated.getStudent().getName());
        res.setStatus(updated.getStatus().name());
        if (updated.getTakenBy() != null) res.setTeacherName(updated.getTakenBy().getName());
        return res;
    }

    // ✅ Get summary — checks room ownership
    @Override
    public AttendanceSummaryResponse getSummary(String roomNumber, LocalDate date) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

        Teacher teacher = getLoggedInTeacher();
        checkRoomAccess(teacher, room);

        Attendance attendance = attendanceRepo.findByRoomIdAndDate(room.getId(), date)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        List<AttendanceRecord> records = recordRepo.findByAttendanceId(attendance.getId());

        int present = 0, absent = 0, late = 0;
        for (AttendanceRecord r : records) {
            switch (r.getStatus()) {
                case PRESENT -> present++;
                case ABSENT -> absent++;
                case LATE -> late++;
            }
        }

        AttendanceSummaryResponse res = new AttendanceSummaryResponse();
        res.setPresent(present);
        res.setAbsent(absent);
        res.setLate(late);
        res.setTotal(records.size());
        return res;
    }

    // ✅ Get today's attendance
    @Override
    public List<AttendanceRecordResponse> getTodayAttendance() {
        LocalDate today = LocalDate.now();
        List<Attendance> sessions = attendanceRepo.findByDate(today);

        List<AttendanceRecordResponse> response = new ArrayList<>();
        for (Attendance a : sessions) {
            List<AttendanceRecord> records = recordRepo.findByAttendanceId(a.getId());
            for (AttendanceRecord r : records) {
                AttendanceRecordResponse res = new AttendanceRecordResponse();
                res.setRecordId(r.getId());
                res.setStudentId(r.getStudent().getId());
                res.setStudentName(r.getStudent().getName());
                res.setStatus(r.getStatus().name());
                if (r.getTakenBy() != null) res.setTeacherName(r.getTakenBy().getName());
                response.add(res);
            }
        }
        return response;
    }

    // ✅ Helper: get logged in teacher
    private Teacher getLoggedInTeacher() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return teacherRepo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    // ✅ Helper: check if teacher can access this room
    private void checkRoomAccess(Teacher teacher, Room room) {
        if (teacher.isHelpMode()) return; // 👈 help mode = access any room

        // check if teacher is assigned to this room
        boolean isAssigned = teacherRoomRepo
                .findByTeacherAndRoom(teacher, room)
                .isPresent();

        if (!isAssigned) {
            throw new RuntimeException(
                "Access denied — you are not assigned to room " + room.getRoomNumber() +
                ". Enable help mode to mark attendance for other rooms."
            );
        }
    }
}