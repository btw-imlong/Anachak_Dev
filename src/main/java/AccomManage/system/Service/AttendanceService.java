package AccomManage.system.Service;

import java.time.LocalDate;
import java.util.List;

import AccomManage.system.Dto.Request.AttendanceRecordUpdateRequest;
import AccomManage.system.Dto.Request.BulkAttendanceUpdateRequest;
import AccomManage.system.Dto.Request.CreateAttendanceRequest;
import AccomManage.system.Dto.Response.AttendanceRecordResponse;
import AccomManage.system.Dto.Response.AttendanceSummaryResponse;

public interface AttendanceService {
    void createAttendance(CreateAttendanceRequest request);
    AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request);
	List<AttendanceRecordResponse> getTodayAttendance();
	void updateBulkAttendance(BulkAttendanceUpdateRequest request);
	List<AttendanceRecordResponse> getAttendanceByRoomAndDate(String roomNumber, LocalDate date);
	AttendanceSummaryResponse getSummary(String roomNumber, LocalDate date);
}
