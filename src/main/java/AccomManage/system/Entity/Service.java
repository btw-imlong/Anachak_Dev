package AccomManage.system.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_gen")
    @SequenceGenerator(name = "service_gen", sequenceName = "service_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String description;
}