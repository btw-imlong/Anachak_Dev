package AccomManage.system.Dto.Response;

import java.util.List;
import lombok.Data;

@Data
public class RoomDetailResponse {
    private Long id;
    private String roomNumber;
    private String side;
    private int totalStudents;
    private List<StudentInfo> students;
    private List<TeacherInfo> teachers;

    @Data
    public static class StudentInfo {
        private Long studentId;
        private String name;
        private String idCardNumber;
    }

    @Data
    public static class TeacherInfo {
        private Long teacherId;
        private String name;
        private String idCardNumber;
    }
}