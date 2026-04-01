package AccomManage.system.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.TaskSchedule;

@Repository
public interface TaskScheduleRepository extends JpaRepository<TaskSchedule, Long> {}
