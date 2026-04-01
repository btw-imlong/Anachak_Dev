package AccomManage.system.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id @GeneratedValue
    private Long id;
   
    private String name;
    private String username;
    @Column(unique=true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;// ADMIN, TEACHER, STUDENT
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
}
