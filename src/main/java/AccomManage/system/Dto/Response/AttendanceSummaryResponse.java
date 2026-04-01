package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class AttendanceSummaryResponse {
    private int present;
    private int absent;
    private int late;
}