package AccomManage.system.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findByRoomNumber(String roomNumber);
}