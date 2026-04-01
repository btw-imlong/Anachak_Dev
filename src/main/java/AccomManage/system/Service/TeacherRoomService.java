package AccomManage.system.Service;

import AccomManage.system.Dto.Request.AssignTeacherRoomRequest;
import AccomManage.system.Dto.Response.AssignTeacherRoomResponse;

public interface TeacherRoomService {
    AssignTeacherRoomResponse assignTeacher(AssignTeacherRoomRequest request);
}