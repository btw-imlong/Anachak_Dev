package AccomManage.system.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Entity.User;

import java.util.List;

public interface UserService {

    // Create
    TeacherResponse createTeacher(CreateTeacherRequest request);
    StudentResponse createStudent(CreateStudentRequest request);

    // Read single
    StudentResponse getStudentById(Long studentId);
    StudentResponse getStudentByUserId(Long userId);
    TeacherResponse getTeacherById(Long teacherId);
    TeacherResponse getTeacherByUserId(Long userId);
    User getUserById(Long id);

    // Read list
    List<User> getAllUsers();
    Page<StudentResponse> getAllStudents(Pageable pageable);
    Page<StudentResponse> getStudentsWithoutRoom(Pageable pageable);
    Page<TeacherResponse> getAllTeachers(Pageable pageable);

    // Update
    StudentResponse updateStudent(Long studentId, UpdateStudentRequest request);
    TeacherResponse updateTeacher(Long teacherId, UpdateTeacherRequest request);

    // Delete
    void deleteUser(Long id);
}