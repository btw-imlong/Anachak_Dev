package AccomManage.system.Dto.Request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MarkTaskDoneRequest {
    private Long taskId;
    private LocalDate completedDate; // which date are you marking done
}