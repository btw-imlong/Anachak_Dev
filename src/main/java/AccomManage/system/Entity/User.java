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
    private String idCardNumber;          // ← add this

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)    // ← add this
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime createdAt = LocalDateTime.now();
}