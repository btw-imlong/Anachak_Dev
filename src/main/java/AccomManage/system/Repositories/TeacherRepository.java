package AccomManage.system.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByUserEmail(String email);
	Optional<Teacher> findByUserId(Long teacherId);
}
