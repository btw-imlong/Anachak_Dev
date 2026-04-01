package AccomManage.system.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Service {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;

    // getters/setters
}
