package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "student_service",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "service_id"}))
public class StudentService {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_service_gen")
    @SequenceGenerator(name = "student_service_gen", sequenceName = "student_service_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private AccomManage.system.Entity.Service service;
}