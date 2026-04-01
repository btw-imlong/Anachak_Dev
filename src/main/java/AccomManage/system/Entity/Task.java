package AccomManage.system.Entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_gen")
    @SequenceGenerator(name = "task_gen", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // MONDAY, TUESDAY ... SUNDAY

    private LocalTime taskTime; // e.g. 05:30, 21:00

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING; // default PENDING

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room; // which room this task belongs to
}