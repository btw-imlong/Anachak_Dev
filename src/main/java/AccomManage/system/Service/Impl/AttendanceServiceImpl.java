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

    // 1️⃣ Create attendance session for a room
    @Override
    public void createAttendance(CreateAttendanceRequest request) {

        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        if (attendanceRepo.existsByRoomIdAndDate(room.getId(), request.getDate())) {
            throw new RuntimeException("Attendance already taken for this room!");
        }

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
            r.setStatus(Status.ABSENT); // default
            records.add(r);
        }
        recordRepo.saveAll(records);
    }

    // 2️⃣ Get all attendance records for a room + date
    @Override
    public List<AttendanceRecordResponse> getAttendanceByRoomAndDate(String roomNumber, LocalDate date) {

        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

        Attendance attendance = attendanceRepo.findByRoomIdAndDate(room.getId(), date)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        List<AttendanceRecord> records = recordRepo.findByAttendanceId(attendance.getId());
        List<AttendanceRecordResponse> response = new ArrayList<>();

        for (AttendanceRecord r : records) {
            AttendanceRecordResponse res = new AttendanceRecordResponse();
            res.setRecordId(r.getId());
            res.setStudentId(r.getStudent().getId());
            res.setStudentName(r.getStudent().getUser().getName());
            res.setStatus(r.getStatus().name());
            if (r.getTakenBy() != null) res.setTeacherName(r.getTakenBy().getUser().getName());
            response.add(res);
        }

        return response;
    }

    // 3️⃣ Bulk update attendance
    @Override
    public void updateBulkAttendance(BulkAttendanceUpdateRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher teacher = teacherRepo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<AttendanceRecord> records = new ArrayList<>();

        for (BulkAttendanceUpdateRequest.RecordUpdate r : request.getRecords()) {

            AttendanceRecord record = recordRepo.findById(r.getRecordId())
                    .orElseThrow(() -> new RuntimeException("Attendance record not found"));

           

            record.setStatus(Status.valueOf(r.getStatus()));
            record.setTakenBy(teacher);

            records.add(record);
        }

        recordRepo.saveAll(records);
    }

    // 4️⃣ Summary for a room
    @Override
    public AttendanceSummaryResponse getSummary(String roomNumber, LocalDate date) {

        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

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

        return res;
    }

	

	@Override
	public AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request) {

	    AttendanceRecord record = recordRepo.findById(recordId)
	            .orElseThrow(() -> new RuntimeException("Attendance record not found"));

	    record.setStatus(Status.valueOf(request.getStatus()));

	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Teacher teacher = teacherRepo.findByUserEmail(email)
	            .orElseThrow(() -> new RuntimeException("Teacher not found"));

	    record.setTakenBy(teacher);

	    AttendanceRecord updated = recordRepo.save(record);

	    AttendanceRecordResponse res = new AttendanceRecordResponse();
	    res.setRecordId(updated.getId());
	    res.setStudentId(updated.getStudent().getId());
	    res.setStudentName(updated.getStudent().getUser().getName());
	    res.setStatus(updated.getStatus().name());
	    if (updated.getTakenBy() != null) res.setTeacherName(updated.getTakenBy().getUser().getName());

	    return res;
	}

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
	            res.setStudentName(r.getStudent().getUser().getName());
	            res.setStatus(r.getStatus().name());
	            if (r.getTakenBy() != null) res.setTeacherName(r.getTakenBy().getUser().getName());
	            response.add(res);
	        }
	    }
	    return response;
	}
}