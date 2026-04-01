package AccomManage.system.Service.Impl;

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

        // 1️⃣ find teacher by User.id
        Teacher teacher = teacherRepo.findByUserId(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // 2️⃣ find room by roomNumber
        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3️⃣ check if room already has a teacher
        TeacherRoom existing = teacherRoomRepo.findByRoom(room).orElse(null);

        TeacherRoom tr;
        if (existing != null) {
            existing.setTeacher(teacher); // replace teacher
            tr = teacherRoomRepo.save(existing);
        } else {
            // create new assignment
            tr = new TeacherRoom();
            tr.setTeacher(teacher);
            tr.setRoom(room);
            tr = teacherRoomRepo.save(tr);
        }

        // 4️⃣ prepare response
        AssignTeacherRoomResponse response = new AssignTeacherRoomResponse();
        response.setTeacherName(tr.getTeacher().getName());
        response.setRoomNumber(tr.getRoom().getRoomNumber());

        return response;
    }
}