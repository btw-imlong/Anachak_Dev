package AccomManage.system.Service;

import java.time.LocalDate;
import java.util.List;

import AccomManage.system.Dto.Request.AttendanceRecordUpdateRequest;
import AccomManage.system.Dto.Request.BulkAttendanceUpdateRequest;
import AccomManage.system.Dto.Request.CreateAttendanceRequest;
import AccomManage.system.Dto.Response.AttendanceRecordResponse;
import AccomManage.system.Dto.Response.AttendanceSummaryResponse;
import AccomManage.system.Dto.Response.ToggleHelpModeResponse;

public interface AttendanceService {
    void createAttendance(CreateAttendanceRequest request);
    List<AttendanceRecordResponse> getAttendanceByRoomAndDate(String roomNumber, LocalDate date);
    void updateBulkAttendance(BulkAttendanceUpdateRequest request);
    AttendanceSummaryResponse getSummary(String roomNumber, LocalDate date);
    AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request);
    List<AttendanceRecordResponse> getTodayAttendance();
    ToggleHelpModeResponse toggleHelpMode(); // 👈 add this
    AttendanceSummaryResponse getTodaySummaryForTeacher();
 // Add to AttendanceService.java
    List<AttendanceRecordResponse> getMyAttendance();
    List<AttendanceRecordResponse> getMyAttendanceByRange(LocalDate from, LocalDate to);
}