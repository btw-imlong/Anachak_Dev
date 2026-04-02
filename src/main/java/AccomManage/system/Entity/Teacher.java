package AccomManage.system.Entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_gen")
    @SequenceGenerator(name = "teacher_gen", sequenceName = "teacher_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String idCardNumber;

    private String name;

    private boolean helpMode = false; // 👈 add this

    @OneToMany(mappedBy = "teacher")
    private List<TeacherRoom> teacherRooms;
}