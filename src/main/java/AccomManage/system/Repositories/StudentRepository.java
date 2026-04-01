package AccomManage.system.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByRoomId(Long roomId);
}