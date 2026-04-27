package AccomManage.system.Dto.Response;

import lombok.Data;
import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String side;
    private List<TeacherInfo> teachers;
    private List<StudentInfo> students; // ← add this

    @Data
    public static class TeacherInfo {
        private Long teacherId;
        private String name;
    }

    @Data
    public static class StudentInfo { // ← add this
        private Long studentId;
        private String name;
        private String idCardNumber;
    }
}