package AccomManage.system.Service;

import java.util.List;

import AccomManage.system.Dto.Request.StudentServiceRequest;
import AccomManage.system.Dto.Response.StudentServiceResponse;
import AccomManage.system.Entity.StudentService;

public interface StudentServiceService {

    StudentServiceResponse assign(StudentService request);

    List<StudentServiceResponse> getByStudent(Long studentId);

    void delete(Long id);

	StudentServiceResponse assign(StudentServiceRequest request);
}