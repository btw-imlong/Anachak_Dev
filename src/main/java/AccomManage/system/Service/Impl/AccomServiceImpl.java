package AccomManage.system.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.AssignServiceRequest;
import AccomManage.system.Dto.Request.CreateServiceRequest;
import AccomManage.system.Dto.Response.ServiceResponse;
import AccomManage.system.Dto.Response.StudentServiceResponse;
import AccomManage.system.Entity.Student;
import AccomManage.system.Entity.StudentService;
import AccomManage.system.Repositories.ServiceRepository;
import AccomManage.system.Repositories.StudentRepository;
import AccomManage.system.Repositories.StudentServiceRepository;
import AccomManage.system.Service.AccomService;

@Service
public class AccomServiceImpl implements AccomService {

    @Autowired private ServiceRepository serviceRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private StudentServiceRepository studentServiceRepo;

    // ✅ Create a new service type (e.g. Library, Canteen)
    @Override
    public ServiceResponse createService(CreateServiceRequest request) {
        if (serviceRepo.existsByNameIgnoreCase(request.getName()))
            throw new RuntimeException("Service already exists: " + request.getName());

        AccomManage.system.Entity.Service service = new AccomManage.system.Entity.Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service = serviceRepo.save(service);

        return mapToServiceResponse(service);
    }

    // ✅ Get all service types
    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepo.findAll()
                .stream()
                .map(this::mapToServiceResponse)
                .collect(Collectors.toList());
    }

    // ✅ Delete a service type
    @Override
    public void deleteService(Long serviceId) {
        AccomManage.system.Entity.Service service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        // block deletion if students are still assigned
        List<StudentService> assignments = studentServiceRepo.findByService(service);
        if (!assignments.isEmpty()) {
            throw new RuntimeException("Cannot delete service — " + assignments.size() + " student(s) still assigned to it. Remove them first.");
        }

        serviceRepo.deleteById(serviceId);
    }

    // ✅ Assign service to student
    @Override
    public StudentServiceResponse assignServiceToStudent(AssignServiceRequest request) {
        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        AccomManage.system.Entity.Service service = serviceRepo.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + request.getServiceId()));

        // check duplicate
        if (studentServiceRepo.existsByStudentAndService(student, service))
            throw new RuntimeException("Student is already assigned to this service");

        StudentService ss = new StudentService();
        ss.setStudent(student);
        ss.setService(service);
        ss = studentServiceRepo.save(ss);

        return mapToStudentServiceResponse(ss);
    }

    // ✅ Remove service from student
    @Override
    public void removeServiceFromStudent(Long studentId, Long serviceId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        AccomManage.system.Entity.Service service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        StudentService ss = studentServiceRepo.findByStudentAndService(student, service)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        studentServiceRepo.delete(ss);
    }

    // ✅ Get all services for a student
    @Override
    public List<StudentServiceResponse> getServicesByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return studentServiceRepo.findByStudent(student)
                .stream()
                .map(this::mapToStudentServiceResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get all students for a service
    @Override
    public List<StudentServiceResponse> getStudentsByService(Long serviceId) {
        AccomManage.system.Entity.Service service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        return studentServiceRepo.findByService(service)
                .stream()
                .map(this::mapToStudentServiceResponse)
                .collect(Collectors.toList());
    }

    // ✅ Helpers
    private ServiceResponse mapToServiceResponse(AccomManage.system.Entity.Service service) {
        ServiceResponse res = new ServiceResponse();
        res.setServiceId(service.getId());
        res.setName(service.getName());
        res.setDescription(service.getDescription());
        return res;
    }

    private StudentServiceResponse mapToStudentServiceResponse(StudentService ss) {
        StudentServiceResponse res = new StudentServiceResponse();
        res.setAssignmentId(ss.getId());
        res.setStudentId(ss.getStudent().getId());
        res.setStudentName(ss.getStudent().getName());
        res.setServiceId(ss.getService().getId());
        res.setServiceName(ss.getService().getName());
        res.setServiceDescription(ss.getService().getDescription());
        return res;
    }
}