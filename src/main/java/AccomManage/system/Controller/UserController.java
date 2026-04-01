package AccomManage.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.CreateStudentRequest;
import AccomManage.system.Dto.Request.CreateTeacherRequest;
import AccomManage.system.Dto.Response.UserResponse;
import AccomManage.system.Entity.User;
import AccomManage.system.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create Teacher
    @PostMapping("/teacher")
    public ResponseEntity<UserResponse> createTeacher(@RequestBody CreateTeacherRequest request) {
        User user = userService.createTeacher(request);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    // Create Student
    @PostMapping("/student")
    public ResponseEntity<UserResponse> createStudent(@RequestBody CreateStudentRequest request) {
        User user = userService.createStudent(request);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> response = users.stream()
            .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()))
            .toList();
        return ResponseEntity.ok(response);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User u = userService.getUserById(id);
        return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
}