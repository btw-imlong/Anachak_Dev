package AccomManage.system.Dto.Response;

import java.util.List;
import lombok.Data;

@Data
public class TeacherResponse {
    private Long id;
    private String name;
    private String email;
    private String idCardNumber;
    private List<RoomInfo> rooms;

    @Data
    public static class RoomInfo {
        private Long roomId;
        private String roomNumber;
        private String side;
    }
}