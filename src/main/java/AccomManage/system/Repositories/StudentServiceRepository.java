package AccomManage.system.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.StudentService;

@Repository
public interface StudentServiceRepository extends JpaRepository<StudentService, Long> {}