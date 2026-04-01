package AccomManage.system.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.AssignServiceRequest;
import AccomManage.system.Dto.Request.CreateServiceRequest;
import AccomManage.system.Dto.Response.ServiceResponse;
import AccomManage.system.Dto.Response.StudentServiceResponse;
import AccomManage.system.Service.AccomService;

@RestController
@RequestMapping("/api/services")
public class AccomServiceController {

    @Autowired
    private AccomService accomService;

    // 🔐 Admin only — manage service types
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ServiceResponse> createService(@RequestBody CreateServiceRequest request) {
        return ResponseEntity.ok(accomService.createService(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(accomService.getAllServices());
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteService(@PathVariable Long serviceId) {
        accomService.deleteService(serviceId);
        return ResponseEntity.ok("Service deleted successfully");
    }

    // 🔐 Admin + Teacher — assign/remove/query
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<StudentServiceResponse> assign(@RequestBody AssignServiceRequest request) {
        return ResponseEntity.ok(accomService.assignServiceToStudent(request));
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> remove(
            @RequestParam Long studentId,
            @RequestParam Long serviceId) {
        accomService.removeServiceFromStudent(studentId, serviceId);
        return ResponseEntity.ok("Service removed from student successfully");
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<StudentServiceResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(accomService.getServicesByStudent(studentId));
    }

    @GetMapping("/{serviceId}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<StudentServiceResponse>> getByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(accomService.getStudentsByService(serviceId));
    }
}