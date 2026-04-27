package AccomManage.system.Dto.Request;

import lombok.Data;

@Data
public class AssignTeacherRoomRequest {
    private Long teacherId;   // this is Teacher's own ID (not user_id)
    private String roomNumber;
}