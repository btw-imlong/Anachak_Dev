package AccomManage.system.SeedData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired UserRepository userRepository;
    @Autowired TeacherRepository teacherRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired TeacherRoomRepository teacherRoomRepository;
    @Autowired AttendanceRecordRepository attendanceRecordRepository;
    @Autowired ServiceRepository serviceRepository;
    @Autowired StudentServiceRepository studentServiceRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired TaskScheduleRepository taskScheduleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentRepository studentRepo;
    
    @Override
    public void run(String... args) throws Exception {
    	if(studentRepo.count() == 0) {
            // seed students
        
        System.out.println("🟢 Running DataInitializer...");

        // ===== USERS =====
        User adminUser = new User();
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin"));
       
        adminUser.setRole(Role.ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());
        
        

        User tUser = new User();
        tUser.setName("Teacher A");
        tUser.setEmail("teacher@example.com");
        tUser.setPassword(passwordEncoder.encode("pass"));
      
        tUser.setRole(Role.TEACHER);
        tUser.setCreatedAt(LocalDateTime.now());

        User sUser = new User();
        sUser.setName("Student One");
        sUser.setEmail("student@example.com");
        sUser.setPassword(passwordEncoder.encode("pass"));
        
        sUser.setRole(Role.STUDENT);
        sUser.setCreatedAt(LocalDateTime.now());

        userRepository.saveAll(List.of(adminUser, tUser, sUser));

        // ===== ROOMS =====
        Room room101 = new Room();
        room101.setRoomNumber("101");
        room101.setSide("Boys");

        Room room102 = new Room();
        room102.setRoomNumber("102");
        room102.setSide("Girls");

        roomRepository.saveAll(List.of(room101, room102));

        // ===== TEACHERS =====
        Teacher teacher = new Teacher();
        teacher.setUser(tUser);
        teacher.setIdCardNumber("TCH123");
        teacherRepository.save(teacher);

        // ===== STUDENTS =====
        Student student = new Student();
        student.setUser(sUser);
        student.setIdCardNumber("STU123");
        student.setRoom(room101);
        studentRepository.save(student);

        // ===== TEACHER_ROOM =====
        TeacherRoom tr = new TeacherRoom();
        tr.setTeacher(teacher);
        tr.setRoom(room101);
        teacherRoomRepository.save(tr);

        // ===== ATTENDANCE =====
        Attendance attendance = new Attendance();
        attendance.setRoom(room101);
        attendance.setDate(LocalDate.now());
        attendance.setCreatedAt(LocalDateTime.now());
        attendanceRepository.save(attendance);

        // ===== ATTENDANCE_RECORD =====
        AttendanceRecord record = new AttendanceRecord();
        record.setAttendance(attendance);
        record.setStudent(student);
        record.setStatus(Status.PRESENT);
        record.setTakenBy(teacher);
        attendanceRecordRepository.save(record);

        // ===== SERVICES =====
        Service laundry = new Service();
        laundry.setName("Laundry");
        laundry.setDescription("Daily laundry service");

        Service cleaning = new Service();
        cleaning.setName("Cleaning");
        cleaning.setDescription("Room cleaning service");

        serviceRepository.saveAll(List.of(laundry, cleaning));

        // ===== STUDENT_SERVICE =====
        StudentService ss = new StudentService();
        ss.setStudent(student);
        ss.setService(laundry);
        studentServiceRepository.save(ss);

        // ===== TASKS =====
        Task morningTask = new Task();
        morningTask.setName("Morning Roll Call");

        Task eveningTask = new Task();
        eveningTask.setName("Evening Clean-up");

        taskRepository.saveAll(List.of(morningTask, eveningTask));

        // ===== TASK_SCHEDULE =====
        TaskSchedule ts = new TaskSchedule();
        ts.setRoom(room101);
        ts.setTask(morningTask);
        ts.setDayOfWeek("Mon");
        ts.setStartDate(LocalDate.now());
        ts.setEndDate(LocalDate.now().plusMonths(1));
        taskScheduleRepository.save(ts);

        System.out.println("✅ Seeded full data: Users, Teachers, Students, Rooms, Attendance, Services, Tasks");
    }
    }
}