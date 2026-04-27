package AccomManage.system.Controller;

import java.util.List;

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

    // POST /api/teacher-room/assign
    @PostMapping("/assign")
    public ResponseEntity<AssignTeacherRoomResponse> assignTeacher(
            @RequestBody AssignTeacherRoomRequest request) {
        return ResponseEntity.ok(teacherRoomService.assignTeacher(request));
    }

    // GET /api/teacher-room/room/{roomNumber}
    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<List<AssignTeacherRoomResponse>> getTeachersByRoom(
            @PathVariable String roomNumber) {
        return ResponseEntity.ok(teacherRoomService.getTeachersByRoom(roomNumber));
    }

    // GET /api/teacher-room/teacher/{teacherId}
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<AssignTeacherRoomResponse>> getRoomsByTeacher(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherRoomService.getRoomsByTeacher(teacherId));
    }

    // DELETE /api/teacher-room/remove?teacherId=1&roomNumber=101
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeTeacherFromRoom(
            @RequestParam Long teacherId,
            @RequestParam String roomNumber) {
        teacherRoomService.removeTeacherFromRoom(teacherId, roomNumber);
        return ResponseEntity.ok("Teacher removed from room successfully");
    }
}