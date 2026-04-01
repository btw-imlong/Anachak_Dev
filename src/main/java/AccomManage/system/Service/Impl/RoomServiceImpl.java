package AccomManage.system.Service.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.CreateRoomRequest;
import AccomManage.system.Dto.Response.RoomDetailResponse;
import AccomManage.system.Dto.Response.RoomResponse;
import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.TeacherRoom;
import AccomManage.system.Repositories.RoomRepository;
import AccomManage.system.Repositories.TeacherRoomRepository;
import AccomManage.system.Service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private TeacherRoomRepository teacherRoomRepo;

    // ✅ Create room
    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        if (roomRepo.findByRoomNumber(request.getRoomNumber()).isPresent())
            throw new RuntimeException("Room already exists: " + request.getRoomNumber());

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setSide(request.getSide());
        Room saved = roomRepo.save(room);
        return mapToRoomResponse(saved);
    }

    // ✅ Get all rooms (simple list)
    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepo.findAll().stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    // ✅ Get room by ID with students and teachers
    @Override
    public RoomDetailResponse getRoomById(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return mapToRoomDetailResponse(room);
    }

    // ✅ Get room by room number with students and teachers
    @Override
    public RoomDetailResponse getRoomByNumber(String roomNumber) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
        return mapToRoomDetailResponse(room);
    }

    // ✅ Get rooms by side (Girls or Boys)
    @Override
    public List<RoomResponse> getRoomsBySide(String side) {
        return roomRepo.findBySideIgnoreCase(side).stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    // ✅ Delete room
    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        // 1. delete teacher_room assignments first
        List<TeacherRoom> teacherRooms = teacherRoomRepo.findByRoom(room);
        teacherRoomRepo.deleteAll(teacherRooms);

        // 2. then delete the room (students + attendances auto deleted via cascade)
        roomRepo.deleteById(id);
    }
    // ✅ Helper: simple room response
    private RoomResponse mapToRoomResponse(Room room) {
        RoomResponse res = new RoomResponse();
        res.setId(room.getId());
        res.setRoomNumber(room.getRoomNumber());
        res.setSide(room.getSide());
        return res;
    }

    // ✅ Helper: detailed room response with students + teachers
    private RoomDetailResponse mapToRoomDetailResponse(Room room) {
        RoomDetailResponse res = new RoomDetailResponse();
        res.setId(room.getId());
        res.setRoomNumber(room.getRoomNumber());
        res.setSide(room.getSide());

        // map students
        List<RoomDetailResponse.StudentInfo> students = room.getStudents() == null
                ? List.of()
                : room.getStudents().stream().map(s -> {
                    RoomDetailResponse.StudentInfo info = new RoomDetailResponse.StudentInfo();
                    info.setStudentId(s.getId());
                    info.setName(s.getName());
                    info.setIdCardNumber(s.getIdCardNumber());
                    return info;
                }).toList();

        // map teachers
        List<RoomDetailResponse.TeacherInfo> teachers = room.getTeacherRooms() == null
                ? List.of()
                : room.getTeacherRooms().stream().map(tr -> {
                    RoomDetailResponse.TeacherInfo info = new RoomDetailResponse.TeacherInfo();
                    info.setTeacherId(tr.getTeacher().getId());
                    info.setName(tr.getTeacher().getName());
                    info.setIdCardNumber(tr.getTeacher().getIdCardNumber());
                    return info;
                }).toList();

        res.setStudents(students);
        res.setTeachers(teachers);
        res.setTotalStudents(students.size());
        return res;
    }
}