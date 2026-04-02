package AccomManage.system.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
@PreAuthorize("hasRole('TEACHER')")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    // ✅ Create attendance session
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody CreateAttendanceRequest request) {
        service.createAttendance(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Attendance created successfully",
                        "room", request.getRoomNumber()
                )
        );
    }

    // ✅ Get all attendance records for a room + date
    @GetMapping
    public ResponseEntity<List<AttendanceRecordResponse>> get(
            @RequestParam String roomNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AttendanceRecordResponse> records =
                service.getAttendanceByRoomAndDate(roomNumber, date);

        return ResponseEntity.ok(records);
    }

    // ✅ Bulk update attendance
    @PutMapping("/bulk")
    public ResponseEntity<Map<String, String>> bulk(@RequestBody BulkAttendanceUpdateRequest request) {
        service.updateBulkAttendance(request);

        return ResponseEntity.ok(
                Map.of("message", "Attendance updated successfully")
        );
    }

    // ✅ Summary of attendance
    @GetMapping("/summary")
    public ResponseEntity<AttendanceSummaryResponse> summary(
            @RequestParam String roomNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        AttendanceSummaryResponse summary =
                service.getSummary(roomNumber, date);

        return ResponseEntity.ok(summary);
    }
 // Add these to AttendanceController.java

    @PutMapping("/{recordId}")
    public ResponseEntity<AttendanceRecordResponse> updateRecord(
            @PathVariable Long recordId,
            @RequestBody AttendanceRecordUpdateRequest request) {
        return ResponseEntity.ok(service.updateAttendanceRecord(recordId, request));
    }

    @GetMapping("/today")
    public ResponseEntity<List<AttendanceRecordResponse>> today() {
        return ResponseEntity.ok(service.getTodayAttendance());
    }
 
 @PatchMapping("/help-mode")
 public ResponseEntity<ToggleHelpModeResponse> toggleHelpMode() {
     return ResponseEntity.ok(service.toggleHelpMode());
 }
}