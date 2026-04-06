package AccomManage.system.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_completion",
       uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "completed_date"}))
public class TaskCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_completion_gen")
    @SequenceGenerator(name = "task_completion_gen", sequenceName = "task_completion_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDate completedDate;
    private LocalDateTime markedAt = LocalDateTime.now();
    private String markedByRole;
    private String markedByName;
}