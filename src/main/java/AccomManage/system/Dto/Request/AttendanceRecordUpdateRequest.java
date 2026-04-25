package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class AttendanceRecordUpdateRequest {
    private String status;
    private String note; 
    // PRESENT, ABSENT, LATE
    // optional, if another teacher marks
}
