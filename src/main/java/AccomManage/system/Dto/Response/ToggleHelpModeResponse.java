package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class ToggleHelpModeResponse {
    private Long teacherId;
    private String teacherName;
    private boolean helpMode;
    private String message;
}