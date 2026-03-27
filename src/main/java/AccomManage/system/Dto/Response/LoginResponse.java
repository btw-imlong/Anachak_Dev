package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class LoginResponse {
	private Long userId;
	private String name;
	private String email;
	private String token;
	private String role;
}
