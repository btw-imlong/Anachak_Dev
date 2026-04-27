package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class AssignTeacherRoomResponse {
    private Long assignmentId;
    private Long teacherId;
    private String teacherName;
    private Long roomId;
    private String roomNumber;
    private String side; // Girls or Boys
    private String message;
}