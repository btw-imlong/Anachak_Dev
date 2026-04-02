package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Student {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(unique=true)
    private String idCardNumber;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_service",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;
}