package AccomManage.system.Dto.Request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CreateAttendanceRequest {
    private String roomNumber; 
    
    @JsonFormat(pattern = "yyyy-MM-dd")// <-- changed from roomId
    private LocalDate date;
}