package AccomManage.system.Repositories;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Task;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {
	@Query("""
		    SELECT COUNT(t) > 0 FROM Task t
		    WHERE t.room.side = :side
		    AND t.dayOfWeek = :dayOfWeek
		    AND LOWER(t.title) = LOWER(:title)
		    AND t.taskTime = :taskTime
		    AND t.room.id != :roomId
		""")
		boolean existsDuplicateOnSameSide(
		    @Param("side") String side,
		    @Param("dayOfWeek") DayOfWeek dayOfWeek,
		    @Param("title") String title,
		    @Param("taskTime") java.time.LocalTime taskTime,
		    @Param("roomId") Long roomId
		);
    List<Task> findByRoomId(Long roomId);
    List<Task> findByRoomIdAndDayOfWeek(Long roomId, DayOfWeek dayOfWeek);
    List<Task> findByDayOfWeek(DayOfWeek dayOfWeek);
    boolean existsByRoomIdAndDayOfWeekAndTaskTimeAndIdNot(
    	    Long roomId,
    	    DayOfWeek dayOfWeek,
    	    java.time.LocalTime taskTime,
    	    Long id
    	);
    boolean existsByRoomIdAndDayOfWeekAndTaskTime(
            Long roomId,
            DayOfWeek dayOfWeek,
            LocalTime taskTime
        );
	
}