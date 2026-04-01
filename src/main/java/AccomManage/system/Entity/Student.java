package AccomManage.system.Entity;





import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

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

    // getters/setters
}