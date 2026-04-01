package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class TaskCompletionResponse {
    private Long completionId;
    private Long taskId;
    private String taskTitle;
    private String completedDate;
    private String markedAt;
    private String markedByRole;
    private String markedByName;
}