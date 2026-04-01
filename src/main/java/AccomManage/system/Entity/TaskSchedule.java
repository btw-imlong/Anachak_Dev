package AccomManage.system.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class TaskSchedule {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;
    private String dayOfWeek; // Mon-Sun
    private LocalDate startDate;
    private LocalDate endDate;

    // getters/setters
}