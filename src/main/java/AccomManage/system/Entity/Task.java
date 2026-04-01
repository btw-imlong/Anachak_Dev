package AccomManage.system.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id @GeneratedValue
    private Long id;
    private String name;
}

    // getters/setters
