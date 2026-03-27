package AccomManage.system.Dto.Request;

import java.time.LocalDate;

import lombok.Data;
@Data
public class CreateAttendanceRequest {
	private Long roomId;
    private LocalDate date;
	
}
