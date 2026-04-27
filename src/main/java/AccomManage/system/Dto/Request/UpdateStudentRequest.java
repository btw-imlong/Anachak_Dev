package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class UpdateStudentRequest {
    private String name;
    private String email;
    private String idCardNumber;
    private String roomNumber; // null = keep current room
}