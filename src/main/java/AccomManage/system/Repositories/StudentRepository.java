package AccomManage.system.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByRoomId(Long roomId);
    Optional<Student> findByUser_Id(Long userId);  // ✅ fixed return type
    Page<Student> findByRoomIsNull(Pageable pageable);
}