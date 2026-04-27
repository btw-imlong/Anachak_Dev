package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private Status status; // PRESENT, ABSENT, LATE

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher takenBy;

    @Column(columnDefinition = "TEXT")
    private String note; 
}