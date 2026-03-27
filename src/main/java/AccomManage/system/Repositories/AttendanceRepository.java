package AccomManage.system.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	List<Attendance> findByRoomId(Long roomId);

    List<Attendance> findByDate(LocalDate date);

    boolean existsByStudentIdAndDate(Long studentId, LocalDate date);
    
    boolean existsByRoomIdAndDate(Long roomId, LocalDate date);
    
    Optional<Attendance> findByRoomIdAndDate(Long roomId, LocalDate date);
    
}