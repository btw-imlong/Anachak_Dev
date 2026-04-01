package AccomManage.system.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.TeacherRoom;

public interface TeacherRoomRepository extends JpaRepository<TeacherRoom, Long> {

    Optional<TeacherRoom> findByRoom(Room room);
}