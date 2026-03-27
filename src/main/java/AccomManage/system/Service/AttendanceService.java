package AccomManage.system.Service;

import java.time.LocalDate;
import java.util.List;

import AccomManage.system.Dto.Request.AttendanceRecordUpdateRequest;
import AccomManage.system.Dto.Request.CreateAttendanceRequest;
import AccomManage.system.Dto.Response.AttendanceRecordResponse;

public interface AttendanceService {
    void createAttendance(CreateAttendanceRequest request);
    List<AttendanceRecordResponse> getAttendanceByRoomAndDate(Long roomId, LocalDate date);
    AttendanceRecordResponse updateAttendanceRecord(Long recordId, AttendanceRecordUpdateRequest request);
	List<AttendanceRecordResponse> getTodayAttendance();
}
