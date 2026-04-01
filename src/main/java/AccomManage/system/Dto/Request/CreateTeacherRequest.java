package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class CreateTeacherRequest {
    private String name;
    private String email;
    private String password;
    private String idCardNumber;
}