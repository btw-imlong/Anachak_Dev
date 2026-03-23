package AccomManage.system.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String name;
    @Column(unique=true)
    private String email;
    private String password;
    private String role; // ADMIN, TEACHER, STUDENT
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
}
