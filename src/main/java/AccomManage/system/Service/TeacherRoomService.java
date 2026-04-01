package AccomManage.system.Service;

import java.util.List;

import AccomManage.system.Dto.Request.AssignTeacherRoomRequest;
import AccomManage.system.Dto.Response.AssignTeacherRoomResponse;

public interface TeacherRoomService {
    AssignTeacherRoomResponse assignTeacher(AssignTeacherRoomRequest request);
    List<AssignTeacherRoomResponse> getTeachersByRoom(String roomNumber);
    List<AssignTeacherRoomResponse> getRoomsByTeacher(Long teacherId);
    void removeTeacherFromRoom(Long teacherId, String roomNumber);
}