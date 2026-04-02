package AccomManage.system.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.*;
import AccomManage.system.Dto.Response.*;
import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;
import AccomManage.system.Security.JwtUtil;
import AccomManage.system.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private TeacherRepository teacherRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    // ── Auth ───────────────────────────────────────────────────────────────────

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

    // ── Create ─────────────────────────────────────────────────────────────────

    @Override
    public TeacherResponse createTeacher(CreateTeacherRequest request) {
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
        teacher = teacherRepo.save(teacher);

        return mapToTeacherResponse(teacher);
    }

    @Override
    public StudentResponse createStudent(CreateStudentRequest request) {
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

        if (request.getRoomNumber() != null && !request.getRoomNumber().isEmpty()) {
            student.setRoom(roomRepo.findByRoomNumber(request.getRoomNumber())
                    .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber())));
        }

        student = studentRepo.save(student);
        return mapToStudentResponse(student);
    }

    // ── Read Single ────────────────────────────────────────────────────────────

    @Override
    public StudentResponse getStudentById(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return mapToStudentResponse(student);
    }

    @Override
    public TeacherResponse getTeacherById(Long teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        return mapToTeacherResponse(teacher);
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // ── Read List ──────────────────────────────────────────────────────────────

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepo.findAll(pageable)
                .map(this::mapToStudentResponse);
    }

    @Override
    public Page<StudentResponse> getStudentsWithoutRoom(Pageable pageable) {
        return studentRepo.findByRoomIsNull(pageable)
                .map(this::mapToStudentResponse);
    }

    @Override
    public Page<TeacherResponse> getAllTeachers(Pageable pageable) {
        return teacherRepo.findAll(pageable)
                .map(this::mapToTeacherResponse);
    }

    // ── Update ─────────────────────────────────────────────────────────────────

    @Override
    public StudentResponse updateStudent(Long studentId, UpdateStudentRequest request) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        User user = student.getUser();

        if (request.getName() != null) {
            student.setName(request.getName());
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            if (userRepo.existsByEmail(request.getEmail()) &&
                !user.getEmail().equals(request.getEmail()))
                throw new RuntimeException("Email already exists");
            user.setEmail(request.getEmail());
        }
        if (request.getIdCardNumber() != null)
            student.setIdCardNumber(request.getIdCardNumber());

        if (request.getRoomNumber() != null && !request.getRoomNumber().isEmpty()) {
            student.setRoom(roomRepo.findByRoomNumber(request.getRoomNumber())
                    .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber())));
        }

        userRepo.save(user);
        student = studentRepo.save(student);
        return mapToStudentResponse(student);
    }

    @Override
    public TeacherResponse updateTeacher(Long teacherId, UpdateTeacherRequest request) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        User user = teacher.getUser();

        if (request.getName() != null) {
            teacher.setName(request.getName());
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            if (userRepo.existsByEmail(request.getEmail()) &&
                !user.getEmail().equals(request.getEmail()))
                throw new RuntimeException("Email already exists");
            user.setEmail(request.getEmail());
        }
        if (request.getIdCardNumber() != null)
            teacher.setIdCardNumber(request.getIdCardNumber());

        userRepo.save(user);
        teacher = teacherRepo.save(teacher);
        return mapToTeacherResponse(teacher);
    }

    // ── Delete ─────────────────────────────────────────────────────────────────

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (user.getRole() == Role.TEACHER) {
            teacherRepo.findByUserId(id).ifPresent(teacherRepo::delete);
        } else if (user.getRole() == Role.STUDENT) {
            studentRepo.findByUserId(id).ifPresent(studentRepo::delete);
        }

        userRepo.deleteById(id);
    }

    // ── Mappers ────────────────────────────────────────────────────────────────

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse res = new StudentResponse();
        res.setId(student.getId());
        res.setName(student.getName());
        res.setEmail(student.getUser().getEmail());
        res.setIdCardNumber(student.getIdCardNumber());

        if (student.getRoom() != null) {
            StudentResponse.RoomInfo roomInfo = new StudentResponse.RoomInfo();
            roomInfo.setRoomId(student.getRoom().getId());
            roomInfo.setRoomNumber(student.getRoom().getRoomNumber());
            roomInfo.setSide(student.getRoom().getSide());
            res.setRoom(roomInfo);
        }
        return res;
    }

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse res = new TeacherResponse();
        res.setId(teacher.getId());
        res.setName(teacher.getName());
        res.setEmail(teacher.getUser().getEmail());
        res.setIdCardNumber(teacher.getIdCardNumber());

        if (teacher.getTeacherRooms() != null) {
            List<TeacherResponse.RoomInfo> rooms = teacher.getTeacherRooms()
                    .stream().map(tr -> {
                        TeacherResponse.RoomInfo info = new TeacherResponse.RoomInfo();
                        info.setRoomId(tr.getRoom().getId());
                        info.setRoomNumber(tr.getRoom().getRoomNumber());
                        info.setSide(tr.getRoom().getSide());
                        return info;
                    }).collect(Collectors.toList());
            res.setRooms(rooms);
        }
        return res;
    }
}