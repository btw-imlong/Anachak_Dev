package AccomManage.system.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    private String roomNumber;
    private String side; // Girls, Boys

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Student> students;
    
    @OneToMany(mappedBy = "room")
    private List<TeacherRoom> teacherRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Attendance> attendances;
}