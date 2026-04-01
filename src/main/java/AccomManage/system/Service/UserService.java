package AccomManage.system.Service;

import java.util.List;
import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Entity.User;

public interface UserService {
    LoginResponse login(LoginRequest request);

    // Create
    StudentResponse createStudent(CreateStudentRequest request);
    TeacherResponse createTeacher(CreateTeacherRequest request);

    // Get all
    List<User> getAllUsers();
    User getUserById(Long id);

    // Get by role with details
    StudentResponse getStudentById(Long studentId);
    TeacherResponse getTeacherById(Long teacherId);

    // Update
    StudentResponse updateStudent(Long studentId, UpdateStudentRequest request);
    TeacherResponse updateTeacher(Long teacherId, UpdateTeacherRequest request);

    // Delete
    void deleteUser(Long id);
}