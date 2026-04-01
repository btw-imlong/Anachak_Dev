package AccomManage.system.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_gen")
    @SequenceGenerator(name = "app_user_gen", sequenceName = "app_user_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, TEACHER, STUDENT

    private LocalDateTime createdAt = LocalDateTime.now();
}