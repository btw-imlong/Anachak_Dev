package AccomManage.system.Dto.Request;

import java.util.List;
import lombok.Data;

@Data
public class AssignStudentsRequest {
    private String roomNumber; // room to assign
    private List<Long> studentIds; // students to assign
}