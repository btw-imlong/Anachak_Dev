// New file: StudentAttendanceController.java
package AccomManage.system.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Response.AttendanceRecordResponse;
import AccomManage.system.Service.AttendanceService;

@RestController
@RequestMapping("/api/student/attendance")
@PreAuthorize("hasRole('STUDENT')")
public class StudentAttendanceController {

    private final AttendanceService service;

    public StudentAttendanceController(AttendanceService service) {
        this.service = service;
    }

    // ✅ Get all my attendance
    @GetMapping
    public ResponseEntity<List<AttendanceRecordResponse>> getMyAttendance() {
        return ResponseEntity.ok(service.getMyAttendance());
    }

    // ✅ Get my attendance filtered by date range
    @GetMapping("/range")
    public ResponseEntity<List<AttendanceRecordResponse>> getMyAttendanceByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(service.getMyAttendanceByRange(from, to));
    }
}