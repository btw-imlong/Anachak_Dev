package AccomManage.system.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.TaskCompletion;

@Repository
public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
    Optional<TaskCompletion> findByTaskIdAndCompletedDate(Long taskId, LocalDate date);
    boolean existsByTaskIdAndCompletedDate(Long taskId, LocalDate date);
    List<TaskCompletion> findByTaskId(Long taskId);
}