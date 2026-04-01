package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class TaskResponse {
    private Long taskId;
    private String title;
    private String description;
    private String dayOfWeek;
    private String taskTime;
    private String status;
    private String roomNumber;
    private String side; // Girls or Boys
}