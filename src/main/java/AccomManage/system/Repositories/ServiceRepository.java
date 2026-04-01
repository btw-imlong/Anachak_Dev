package AccomManage.system.Repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import AccomManage.system.Entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}