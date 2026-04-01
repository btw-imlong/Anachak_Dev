package AccomManage.system.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Entity.User;
import AccomManage.system.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    // 🔐 Create teacher - Admin only
    @PostMapping("/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody CreateTeacherRequest request) {
        return ResponseEntity.ok(userService.createTeacher(request));
    }

    // 🔐 Create student - Admin only
    @PostMapping("/student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody CreateStudentRequest request) {
        return ResponseEntity.ok(userService.createStudent(request));
    }

    // 🔐 Get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> response = users.stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔐 Get student by ID with room info
    @GetMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getStudentById(id));
    }

    // 🔐 Get teacher by ID with rooms info
    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTeacherById(id));
    }

    // 🔐 Update student
    @PutMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(userService.updateStudent(id, request));
    }

    // 🔐 Update teacher
    @PutMapping("/teacher/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable Long id,
            @RequestBody UpdateTeacherRequest request) {
        return ResponseEntity.ok(userService.updateTeacher(id, request));
    }

    // 🔐 Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}