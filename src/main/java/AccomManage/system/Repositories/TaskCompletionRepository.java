package AccomManage.system.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import AccomManage.system.Entity.TaskCompletion;

@Repository
public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {

    Optional<TaskCompletion> findByTaskIdAndCompletedDate(Long taskId, LocalDate date);

    boolean existsByTaskIdAndCompletedDate(Long taskId, LocalDate date);

    List<TaskCompletion> findByTaskId(Long taskId);

    void deleteByTaskIdAndCompletedDate(Long taskId, LocalDate date);

    @Query("""
            SELECT c FROM TaskCompletion c
            WHERE c.task.room.roomNumber = :roomNumber
            AND c.completedDate BETWEEN :from AND :to
            """)
    List<TaskCompletion> findByRoomNumberAndDateRange(
            @Param("roomNumber") String roomNumber,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
            SELECT c FROM TaskCompletion c
            WHERE c.completedDate BETWEEN :from AND :to
            """)
    List<TaskCompletion> findAllByDateRange(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}