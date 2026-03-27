package AccomManage.system.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class AttendanceRecord {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name="attendance_id")
    private Attendance attendance;
    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;
    private String status; // Present, Absent, Late
    @ManyToOne
    @JoinColumn(name="taken_by_teacher_id")
    private Teacher takenBy;

    // getters/setters
}