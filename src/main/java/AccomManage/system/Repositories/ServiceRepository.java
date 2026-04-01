package AccomManage.system.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {}