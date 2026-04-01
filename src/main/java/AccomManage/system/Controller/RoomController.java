package AccomManage.system.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.AssignStudentsRequest;
import AccomManage.system.Dto.Request.CreateRoomRequest;
import AccomManage.system.Dto.Response.RoomDetailResponse;
import AccomManage.system.Dto.Response.RoomResponse;
import AccomManage.system.Service.Impl.RoomAssignmentService;
import AccomManage.system.Service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired private RoomService roomService;
    @Autowired private RoomAssignmentService roomAssignmentService;

    // 🔐 Create room - Admin only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    // 🔐 Get all rooms
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // 🔐 Get room by ID with students + teachers
    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<RoomDetailResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // 🔐 Get room by room number with students + teachers
    @GetMapping("/number/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<RoomDetailResponse> getRoomByNumber(@PathVariable String roomNumber) {
        return ResponseEntity.ok(roomService.getRoomByNumber(roomNumber));
    }

    // 🔐 Get rooms by side (Girls or Boys)
    @GetMapping("/side/{side}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<RoomResponse>> getRoomsBySide(@PathVariable String side) {
        return ResponseEntity.ok(roomService.getRoomsBySide(side));
    }

    // 🔐 Assign students to room
    @PostMapping("/assign-students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> assignStudents(@RequestBody AssignStudentsRequest request) {
        roomAssignmentService.assignStudents(request);
        return ResponseEntity.ok("Students assigned to room " + request.getRoomNumber());
    }

    // 🔐 Delete room - Admin only
    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}