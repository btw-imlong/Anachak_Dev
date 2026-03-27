package AccomManage.system.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.CreateStudentRequest;
import AccomManage.system.Dto.Request.CreateTeacherRequest;
import AccomManage.system.Dto.Request.LoginRequest;
import AccomManage.system.Dto.Response.LoginResponse;
import AccomManage.system.Entity.Role;
import AccomManage.system.Entity.Student;
import AccomManage.system.Entity.Teacher;
import AccomManage.system.Entity.User;
import AccomManage.system.Repositories.RoomRepository;
import AccomManage.system.Repositories.StudentRepository;
import AccomManage.system.Repositories.TeacherRepository;
import AccomManage.system.Repositories.UserRepository;
import AccomManage.system.Security.JwtUtil;
import AccomManage.system.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private JwtUtil jwtUtil;

     @Override
    public User createTeacher(CreateTeacherRequest request) {
        if (userRepo.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TEACHER);
        user = userRepo.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setName(request.getName());
        teacher.setIdCardNumber(request.getIdCardNumber());
        teacherRepo.save(teacher);

        return user;
    }

    @Override
    public User createStudent(CreateStudentRequest request) {
        if (userRepo.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user = userRepo.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setName(request.getName());
        student.setIdCardNumber(request.getIdCardNumber());

        if (request.getRoomId() != null) {
            student.setRoom(roomRepo.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found")));
        }

        studentRepo.save(student);
        return user;
    }
    // other CRUD methods
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return response;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}