package AccomManage.system.Dto.Request;

import java.util.List;
import lombok.Data;

@Data
public class BulkAttendanceUpdateRequest {

    private List<RecordUpdate> records;

    @Data
    public static class RecordUpdate {
        private Long recordId;
        private String status; 
        private String note;// or Status if you want strict
    }
}