package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "teacher_room")
public class TeacherRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_room_gen")
	@SequenceGenerator(name = "teacher_room_gen", sequenceName = "teacher_room_seq", allocationSize = 1)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "room_id") // removed unique=true to allow multiple teachers per room
    private Room room;
}