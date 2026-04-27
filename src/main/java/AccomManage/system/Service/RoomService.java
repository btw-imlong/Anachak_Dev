package AccomManage.system.Service;

import java.util.List;
import AccomManage.system.Dto.Request.CreateRoomRequest;
import AccomManage.system.Dto.Response.RoomDetailResponse;
import AccomManage.system.Dto.Response.RoomResponse;

public interface RoomService {
    RoomResponse createRoom(CreateRoomRequest request);
    List<RoomResponse> getAllRooms();
    RoomDetailResponse getRoomById(Long id);
    RoomDetailResponse getRoomByNumber(String roomNumber);
    List<RoomResponse> getRoomsBySide(String side);
    void deleteRoom(Long id);
  
}