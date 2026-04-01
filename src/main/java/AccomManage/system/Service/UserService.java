package AccomManage.system.Service;

import java.util.List;

import AccomManage.system.Dto.Request.CreateStudentRequest;
import AccomManage.system.Dto.Request.CreateTeacherRequest;

import AccomManage.system.Dto.Request.LoginRequest;
import AccomManage.system.Dto.Response.LoginResponse;
import AccomManage.system.Entity.User;

public interface UserService {
	    LoginResponse login(LoginRequest request);
	    
	    List<User> getAllUsers();
	    User getUserById(Long id);
	    void deleteUser(Long id);
		User createTeacher(CreateTeacherRequest request);

		User createStudent(CreateStudentRequest request);
}