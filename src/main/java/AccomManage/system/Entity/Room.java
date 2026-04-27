package AccomManage.system.Entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_gen")
    @SequenceGenerator(name = "room_gen", sequenceName = "room_seq", allocationSize = 1)
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