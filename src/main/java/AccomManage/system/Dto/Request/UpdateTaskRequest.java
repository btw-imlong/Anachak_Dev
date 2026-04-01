package AccomManage.system.Dto.Request;

import java.time.LocalTime;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private String dayOfWeek;
    private LocalTime taskTime;
}