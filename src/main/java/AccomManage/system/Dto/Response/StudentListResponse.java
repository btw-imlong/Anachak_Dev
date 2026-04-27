package AccomManage.system.Dto.Response;

import lombok.Data;

// Used for GET /api/users/students (list all students with room info)
@Data
public class StudentListResponse {
    private Long id;
    private String name;
    private String email;
    private String idCardNumber;
    private RoomInfo room; // null if not assigned

    @Data
    public static class RoomInfo {
        private Long roomId;
        private String roomNumber;
        private String side;
    }
}