package AccomManage.system.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import AccomManage.system.Dto.Request.AssignStudentsRequest;
import AccomManage.system.Service.Impl.RoomAssignmentService;

@RestController
@RequestMapping("/room")
public class RoomAssignmentController {

    @Autowired private RoomAssignmentService service;

    @PostMapping("/assign-students")
    public ResponseEntity<?> assignStudents(@RequestBody AssignStudentsRequest request) {
        service.assignStudents(request);
        return ResponseEntity.ok("Students assigned to room " + request.getRoomNumber());
    }
}