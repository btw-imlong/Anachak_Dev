package AccomManage.system.Dto.Response;

import lombok.Data;

@Data
public class StudentServiceResponse {
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
}