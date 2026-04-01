package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class AttendanceRecordUpdateRequest {
    private String status;       // PRESENT, ABSENT, LATE
      // optional, if another teacher marks
}
