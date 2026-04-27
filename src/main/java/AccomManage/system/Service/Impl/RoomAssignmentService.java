package AccomManage.system.Service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import AccomManage.system.Dto.Request.AssignStudentsRequest;
import AccomManage.system.Entity.*;
import AccomManage.system.Repositories.*;

@Service
public class RoomAssignmentService {

    @Autowired private RoomRepository roomRepo;
    @Autowired private StudentRepository studentRepo;

    public void assignStudents(AssignStudentsRequest request) {
        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        List<Student> students = studentRepo.findAllById(request.getStudentIds());
        if (students.isEmpty()) throw new RuntimeException("No valid students found!");

        for (Student s : students) {
            s.setRoom(room);
        }
        studentRepo.saveAll(students);
    }
    public void removeStudents(AssignStudentsRequest request) {
        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        List<Student> students = studentRepo.findAllById(request.getStudentIds());
        if (students.isEmpty()) throw new RuntimeException("No valid students found!");

        for (Student s : students) {
            if (s.getRoom() != null && s.getRoom().getId().equals(room.getId())) {
                s.setRoom(null);
            }
        }
        studentRepo.saveAll(students);
    }
    
}