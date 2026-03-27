package AccomManage.system.Service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.AttendanceRecordUpdateRequest;
import AccomManage.system.Dto.Request.CreateAttendanceRequest;
import AccomManage.system.Dto.Response.AttendanceRecordResponse;
import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;
import AccomManage.system.Service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private AttendanceRecordRepository recordRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    // 1️⃣ Create attendance session for a room
    @Override
    public void createAttendance(CreateAttendanceRequest request) {
        if (attendanceRepo.existsByRoomIdAndDate(request.getRoomId(), request.getDate())) {
            throw new RuntimeException("Attendance already created for this room and date!");
        }

        Room room = roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

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
            r.setStatus("ABSENT"); // default
            records.add(r);
        }
        recordRepo.saveAll(records);
    }

    // 2️⃣ Get all attendance records for a room + date
    @Override
    public List<AttendanceRecordResponse> getAttendanceByRoomAndDate(Long roomId, LocalDate date) {
        Attendance attendance = attendanceRepo.findByRoomIdAndDate(roomId, date)
                .orElseThrow(() -> new RuntimeException("Attendance session not found"));

        List<AttendanceRecord> records = recordRepo.findByAttendanceId(attendance.getId());

        List<AttendanceRecordResponse> response = new ArrayList<>();
        for (AttendanceRecord r : records) {
            AttendanceRecordResponse res = new AttendanceRecordResponse();
            res.setRecordId(r.getId());
            res.setStudentId(r.getStudent().getId());
            res.setStudentName(r.getStudent().getUser().getName());
            res.setStatus(r.getStatus());
            if (r.getTakenBy() != null) {
                res.setTeacherName(r.getTakenBy().getUser().getName());
            }
            response.add(res);
        }
        return response;
    }

    // 3️⃣ Update single student's attendance
   @Override
public AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request) {
    AttendanceRecord record = recordRepo.findById(recordId)
            .orElseThrow(() -> new RuntimeException("Attendance record not found"));

    record.setStatus(request.getStatus());

    // ✅ Get teacher from logged-in user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String usernameOrEmail = auth.getName();

    Teacher teacher = teacherRepo.findByUserEmail(usernameOrEmail) // or findByUserName() depending on your field
            .orElseThrow(() -> new RuntimeException("Teacher not found"));

    record.setTakenBy(teacher);

    AttendanceRecord updated = recordRepo.save(record);

    // Map to response
    AttendanceRecordResponse res = new AttendanceRecordResponse();
    res.setRecordId(updated.getId());
    res.setStudentId(updated.getStudent().getId());
    res.setStudentName(updated.getStudent().getUser().getName());
    res.setStatus(updated.getStatus());
    res.setTeacherName(updated.getTakenBy().getName()); // 🔥 safe

    return res;
}
    // 4️⃣ Admin view: get all attendance records today
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
                res.setStatus(r.getStatus());
                if (r.getTakenBy() != null) {
                    res.setTeacherName(r.getTakenBy().getUser().getName());
                }
                response.add(res);
            }
        }
        return response;
    }
}