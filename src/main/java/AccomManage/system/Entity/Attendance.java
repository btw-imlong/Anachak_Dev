package AccomManage.system.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance",
uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "date"}))
public class Attendance {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private LocalDate date;

@Enumerated(EnumType.STRING)
private Status status;

@ManyToOne
@JoinColumn(name = "student_id")
private Student student;

@ManyToOne
@JoinColumn(name = "room_id")
private Room room;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // 
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL)
    private List<AttendanceRecord> records;

    // getters/setters
}