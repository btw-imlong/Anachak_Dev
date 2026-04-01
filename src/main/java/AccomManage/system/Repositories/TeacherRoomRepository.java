package AccomManage.system.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.Teacher;
import AccomManage.system.Entity.TeacherRoom;

@Repository
public interface TeacherRoomRepository extends JpaRepository<TeacherRoom, Long> {

    // get all assignments for a room (multiple teachers allowed)
    List<TeacherRoom> findByRoom(Room room);

    // get all rooms assigned to a teacher
    List<TeacherRoom> findByTeacher(Teacher teacher);

    // check if this specific teacher is already assigned to this room
    Optional<TeacherRoom> findByTeacherAndRoom(Teacher teacher, Room room);
}