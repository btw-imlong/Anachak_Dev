package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private String idCardNumber;
    private RoomInfo room;

    @Data
    public static class RoomInfo {
        private Long roomId;
        private String roomNumber;
        private String side;
    }
}