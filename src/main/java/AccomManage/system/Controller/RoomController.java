package AccomManage.system.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.CreateRoomRequest;
import AccomManage.system.Dto.Response.RoomResponse;
import AccomManage.system.Entity.Room;
import AccomManage.system.Service.Impl.RoomServiceImpl;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomServiceImpl roomService;

    // ✅ Create Room
    @PostMapping
    public Room createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    // ✅ Get All Rooms
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    // ✅ Get Room by ID
    @GetMapping("/{id}")
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // ✅ Delete Room
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}