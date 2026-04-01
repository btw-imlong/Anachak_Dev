package AccomManage.system.Dto.Request;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private String title;
    private String description;
    private String dayOfWeek;  // "MONDAY", "TUESDAY" etc.
    private LocalTime taskTime; // "05:30:00" or "21:00:00"
    private String roomNumber;
}