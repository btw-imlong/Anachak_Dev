package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private String role; // ADMIN, TEACHER, STUDENT
}