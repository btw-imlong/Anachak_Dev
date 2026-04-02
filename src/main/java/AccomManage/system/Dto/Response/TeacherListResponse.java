package AccomManage.system.Dto.Response;

import lombok.Data;
import java.util.List;

// Used for GET /api/users/teachers (list all teachers with their rooms)
@Data
public class TeacherListResponse {
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