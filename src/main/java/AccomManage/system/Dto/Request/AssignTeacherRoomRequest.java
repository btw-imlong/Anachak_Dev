package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class AssignTeacherRoomRequest {
    private Long teacherId;
    private String roomNumber;
}