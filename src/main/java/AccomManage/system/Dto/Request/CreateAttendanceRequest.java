package AccomManage.system.Dto.Request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateAttendanceRequest {
    private String roomNumber;  // <-- changed from roomId
    private LocalDate date;
}