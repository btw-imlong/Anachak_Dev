package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeacherRoom {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name="room_id", unique = true) // one teacher per room
    private Room room;
}