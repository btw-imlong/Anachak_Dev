package AccomManage.system.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.AssignTeacherRoomRequest;
import AccomManage.system.Dto.Response.AssignTeacherRoomResponse;
import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.Teacher;
import AccomManage.system.Entity.TeacherRoom;
import AccomManage.system.Repositories.RoomRepository;
import AccomManage.system.Repositories.TeacherRepository;
import AccomManage.system.Repositories.TeacherRoomRepository;
import AccomManage.system.Service.TeacherRoomService;

@Service
public class TeacherRoomServiceImpl implements TeacherRoomService {

    @Autowired
    private TeacherRoomRepository teacherRoomRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private RoomRepository roomRepo;

  
    @Override
    public AssignTeacherRoomResponse assignTeacher(AssignTeacherRoomRequest request) {

        Teacher teacher = teacherRepo.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getTeacherId()));

        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + request.getRoomNumber()));

        // ✅ Check if room already has a teacher assigned
        List<TeacherRoom> existing = teacherRoomRepo.findByRoom(room);
        if (!existing.isEmpty()) {
            throw new RuntimeException("Room " + request.getRoomNumber() + " already has a teacher assigned");
        }

        // Check duplicate assignment
        teacherRoomRepo.findByTeacherAndRoom(teacher, room).ifPresent(e -> {
            throw new RuntimeException("Teacher is already assigned to this room");
        });

        TeacherRoom tr = new TeacherRoom();
        tr.setTeacher(teacher);
        tr.setRoom(room);
        tr = teacherRoomRepo.save(tr);

        return mapToResponse(tr, "Teacher assigned to room successfully");
    }

    // ✅ Get all teachers in a room
    @Override
    public List<AssignTeacherRoomResponse> getTeachersByRoom(String roomNumber) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));

        return teacherRoomRepo.findByRoom(room)
                .stream()
                .map(tr -> mapToResponse(tr, null))
                .collect(Collectors.toList());
    }

    // ✅ Get all rooms assigned to a teacher
    @Override
    public List<AssignTeacherRoomResponse> getRoomsByTeacher(Long teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        return teacherRoomRepo.findByTeacher(teacher)
                .stream()
                .map(tr -> mapToResponse(tr, null))
                .collect(Collectors.toList());
    }

    // ✅ Remove teacher from room
    @Override
    public void removeTeacherFromRoom(Long teacherId, String roomNumber) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));

        TeacherRoom tr = teacherRoomRepo.findByTeacherAndRoom(teacher, room)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        teacherRoomRepo.delete(tr);
    }

    // ✅ Helper: map entity to response
    private AssignTeacherRoomResponse mapToResponse(TeacherRoom tr, String message) {
        AssignTeacherRoomResponse response = new AssignTeacherRoomResponse();
        response.setAssignmentId(tr.getId());
        response.setTeacherId(tr.getTeacher().getId());
        response.setTeacherName(tr.getTeacher().getName());
        response.setRoomId(tr.getRoom().getId());
        response.setRoomNumber(tr.getRoom().getRoomNumber());
        response.setSide(tr.getRoom().getSide());
        response.setMessage(message);
        return response;
    }
}