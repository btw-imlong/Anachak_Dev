package AccomManage.system.Service;

import java.util.List;
import AccomManage.system.Dto.Request.AssignServiceRequest;
import AccomManage.system.Dto.Request.CreateServiceRequest;
import AccomManage.system.Dto.Response.ServiceResponse;
import AccomManage.system.Dto.Response.StudentServiceResponse;

public interface AccomService {
    // Manage service types
    ServiceResponse createService(CreateServiceRequest request);
    List<ServiceResponse> getAllServices();
    void deleteService(Long serviceId);

    // Assign / remove
    StudentServiceResponse assignServiceToStudent(AssignServiceRequest request);
    void removeServiceFromStudent(Long studentId, Long serviceId);

    // Query
    List<StudentServiceResponse> getServicesByStudent(Long studentId);
    List<StudentServiceResponse> getStudentsByService(Long serviceId);
}