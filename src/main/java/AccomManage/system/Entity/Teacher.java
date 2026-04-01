package AccomManage.system.Entity;



import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Teacher {
    @Id @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id")
    
    private User user;
    @Column(unique=true)
    private String idCardNumber;
    private String name;
    @OneToMany(mappedBy = "teacher")
    private List<TeacherRoom> teacherRooms;
    // getters/setters
}
