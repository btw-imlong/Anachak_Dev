package AccomManage.system.SeedData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired private TeacherRoomRepository teacherRoomRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private StudentServiceRepository studentServiceRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("⏭️ Data already exists, skipping seed.");
            return;
        }

        System.out.println("🟢 Running DataInitializer...");

        // ===== USERS =====
        User adminUser = new User();
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());

        User teacherUser = new User();
        teacherUser.setName("Teacher A");
        teacherUser.setEmail("teacher@example.com");
        teacherUser.setPassword(passwordEncoder.encode("pass"));
        teacherUser.setRole(Role.TEACHER);
        teacherUser.setCreatedAt(LocalDateTime.now());

        User studentUser1 = new User();
        studentUser1.setName("Student One");
        studentUser1.setEmail("student1@example.com");
        studentUser1.setPassword(passwordEncoder.encode("pass"));
        studentUser1.setRole(Role.STUDENT);
        studentUser1.setCreatedAt(LocalDateTime.now());

        User studentUser2 = new User();
        studentUser2.setName("Student Two");
        studentUser2.setEmail("student2@example.com");
        studentUser2.setPassword(passwordEncoder.encode("pass"));
        studentUser2.setRole(Role.STUDENT);
        studentUser2.setCreatedAt(LocalDateTime.now());

        userRepository.saveAll(List.of(adminUser, teacherUser, studentUser1, studentUser2));

        // ===== ROOMS =====
        Room boysRoom = new Room();
        boysRoom.setRoomNumber("101");
        boysRoom.setSide("Boys");

        Room girlsRoom = new Room();
        girlsRoom.setRoomNumber("102");
        girlsRoom.setSide("Girls");

        roomRepository.saveAll(List.of(boysRoom, girlsRoom));

        // ===== TEACHER =====
        Teacher teacher = new Teacher();
        teacher.setUser(teacherUser);
        teacher.setName(teacherUser.getName());
        teacher.setIdCardNumber("TCH001");
        teacherRepository.save(teacher);

        // ===== STUDENTS =====
        Student student1 = new Student();
        student1.setUser(studentUser1);
        student1.setName(studentUser1.getName());
        student1.setIdCardNumber("STU001");
        student1.setRoom(boysRoom);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setUser(studentUser2);
        student2.setName(studentUser2.getName());
        student2.setIdCardNumber("STU002");
        student2.setRoom(girlsRoom);
        studentRepository.save(student2);

        // ===== TEACHER ROOM =====
        TeacherRoom tr1 = new TeacherRoom();
        tr1.setTeacher(teacher);
        tr1.setRoom(boysRoom);
        teacherRoomRepository.save(tr1);

        TeacherRoom tr2 = new TeacherRoom();
        tr2.setTeacher(teacher);
        tr2.setRoom(girlsRoom);
        teacherRoomRepository.save(tr2);

        // ===== ATTENDANCE =====
        Attendance attendance = new Attendance();
        attendance.setRoom(boysRoom);
        attendance.setDate(LocalDate.now());
        attendance.setCreatedAt(LocalDateTime.now());
        attendanceRepository.save(attendance);

        // ===== ATTENDANCE RECORDS =====
        AttendanceRecord record = new AttendanceRecord();
        record.setAttendance(attendance);
        record.setStudent(student1);
        record.setStatus(Status.PRESENT);
        record.setTakenBy(teacher);
        attendanceRecordRepository.save(record);

        // ===== SERVICES =====
        Service library = new Service();
        library.setName("Library");
        library.setDescription("Access to school library");

        Service canteen = new Service();
        canteen.setName("Canteen");
        canteen.setDescription("Daily meal service");

        Service garden = new Service();
        garden.setName("Garden");
        garden.setDescription("Gardening activity");

        Service childcare = new Service();
        childcare.setName("Childcare");
        childcare.setDescription("After school childcare");

        serviceRepository.saveAll(List.of(library, canteen, garden, childcare));

        // ===== STUDENT SERVICES =====
        StudentService ss1 = new StudentService();
        ss1.setStudent(student1);
        ss1.setService(library);
        studentServiceRepository.save(ss1);

        StudentService ss2 = new StudentService();
        ss2.setStudent(student1);
        ss2.setService(canteen);
        studentServiceRepository.save(ss2);

        StudentService ss3 = new StudentService();
        ss3.setStudent(student2);
        ss3.setService(garden);
        studentServiceRepository.save(ss3);

        // ===== TASKS (Boys Room 101) =====
        Task boysMorningMon = new Task();
        boysMorningMon.setTitle("Morning Roll Call");
        boysMorningMon.setDescription("All boys gather at the front yard for roll call");
        boysMorningMon.setDayOfWeek(DayOfWeek.MONDAY);
        boysMorningMon.setTaskTime(LocalTime.of(5, 30));
       
        boysMorningMon.setRoom(boysRoom);

        Task boysEveningMon = new Task();
        boysEveningMon.setTitle("Evening Room Cleanup");
        boysEveningMon.setDescription("Clean and tidy the boys dormitory");
        boysEveningMon.setDayOfWeek(DayOfWeek.MONDAY);
        boysEveningMon.setTaskTime(LocalTime.of(21, 0));
 
        boysEveningMon.setRoom(boysRoom);

        Task boysMorningTue = new Task();
        boysMorningTue.setTitle("Morning Exercise");
        boysMorningTue.setDescription("Morning exercise session at the field");
        boysMorningTue.setDayOfWeek(DayOfWeek.TUESDAY);
        boysMorningTue.setTaskTime(LocalTime.of(5, 30));
   
        boysMorningTue.setRoom(boysRoom);

        // ===== TASKS (Girls Room 102) =====
        Task girlsMorningMon = new Task();
        girlsMorningMon.setTitle("Morning Roll Call");
        girlsMorningMon.setDescription("All girls gather at the garden for roll call");
        girlsMorningMon.setDayOfWeek(DayOfWeek.MONDAY);
        girlsMorningMon.setTaskTime(LocalTime.of(5, 30));
      
        girlsMorningMon.setRoom(girlsRoom);

        Task girlsEveningMon = new Task();
        girlsEveningMon.setTitle("Evening Room Cleanup");
        girlsEveningMon.setDescription("Clean and tidy the girls dormitory");
        girlsEveningMon.setDayOfWeek(DayOfWeek.MONDAY);
        girlsEveningMon.setTaskTime(LocalTime.of(21, 0));
      
        girlsEveningMon.setRoom(girlsRoom);

        Task girlsMorningTue = new Task();
        girlsMorningTue.setTitle("Morning Exercise");
        girlsMorningTue.setDescription("Morning exercise session at the girls court");
        girlsMorningTue.setDayOfWeek(DayOfWeek.TUESDAY);
        girlsMorningTue.setTaskTime(LocalTime.of(5, 30));
      
        girlsMorningTue.setRoom(girlsRoom);

        taskRepository.saveAll(List.of(
            boysMorningMon, boysEveningMon, boysMorningTue,
            girlsMorningMon, girlsEveningMon, girlsMorningTue
        ));

        System.out.println("✅ Seed complete: Users, Rooms, Teachers, Students, Attendance, Services, Tasks");
    }
}