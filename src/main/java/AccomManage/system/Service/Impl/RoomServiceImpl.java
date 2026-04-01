package AccomManage.system.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.CreateRoomRequest;
import AccomManage.system.Dto.Response.RoomResponse;
import AccomManage.system.Entity.Room;
import AccomManage.system.Repositories.RoomRepository;
import AccomManage.system.Service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    public Room createRoom(CreateRoomRequest request) {
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setSide(request.getSide());

        return roomRepo.save(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepo.findAll().stream().map(room -> {
            RoomResponse res = new RoomResponse();
            res.setId(room.getId());
            res.setRoomNumber(room.getRoomNumber());
            res.setSide(room.getSide());
            return res;
        }).toList();
    }

    public Room getRoomById(Long id) {
        return roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public void deleteRoom(Long id) {
        roomRepo.deleteById(id);
    }
    
    
}