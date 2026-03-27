package AccomManage.system.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.AttendanceRecordUpdateRequest;
import AccomManage.system.Dto.Request.CreateAttendanceRequest;
import AccomManage.system.Dto.Response.AttendanceRecordResponse;
import AccomManage.system.Service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAttendanceRequest request) {
        service.createAttendance(request);
        return ResponseEntity.ok("Attendance created successfully");
    }

    @GetMapping
    public ResponseEntity<List<AttendanceRecordResponse>> getByRoomAndDate(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AttendanceRecordResponse> list = service.getAttendanceByRoomAndDate(roomId, date);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/record/{id}")
    public ResponseEntity<AttendanceRecordResponse> updateRecord(
            @PathVariable Long id,
            @RequestBody AttendanceRecordUpdateRequest request) {

        AttendanceRecordResponse response = service.updateAttendanceRecord(id, request);
        return ResponseEntity.ok(response);
    }
}