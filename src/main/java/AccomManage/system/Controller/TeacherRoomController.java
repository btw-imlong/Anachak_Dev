package AccomManage.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.AssignTeacherRoomRequest;
import AccomManage.system.Dto.Response.AssignTeacherRoomResponse;
import AccomManage.system.Service.TeacherRoomService;

@RestController
@RequestMapping("/api/teacher-room")
public class TeacherRoomController {

    @Autowired
    private TeacherRoomService teacherRoomService;

    @PostMapping("/assign")
    public ResponseEntity<AssignTeacherRoomResponse> assignTeacher(@RequestBody AssignTeacherRoomRequest request) {
        AssignTeacherRoomResponse response = teacherRoomService.assignTeacher(request);
        return ResponseEntity.ok(response);
    }
}