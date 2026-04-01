package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class UpdateTeacherRequest {
    private String name;
    private String email;
    private String idCardNumber;
}