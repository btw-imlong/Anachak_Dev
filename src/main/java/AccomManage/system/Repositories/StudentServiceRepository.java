package AccomManage.system.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import AccomManage.system.Entity.Service;
import AccomManage.system.Entity.Student;
import AccomManage.system.Entity.StudentService;

@Repository
public interface StudentServiceRepository extends JpaRepository<StudentService, Long> {
    List<StudentService> findByStudent(Student student);
    List<StudentService> findByService(Service service);
    Optional<StudentService> findByStudentAndService(Student student, Service service);
    boolean existsByStudentAndService(Student student, Service service);
}