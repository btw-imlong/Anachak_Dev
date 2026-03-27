package AccomManage.system.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
