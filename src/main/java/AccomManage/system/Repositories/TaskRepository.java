package AccomManage.system.Repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByRoomId(Long roomId);
    List<Task> findByRoomIdAndDayOfWeek(Long roomId, DayOfWeek dayOfWeek);
    List<Task> findByDayOfWeek(DayOfWeek dayOfWeek);
}