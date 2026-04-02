package AccomManage.system.Dto.Response;

import lombok.Data;
import java.util.List;

@Data
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private String idCardNumber;
    private RoomInfo room;
    private List<ServiceInfo> services;

    @Data
    public static class RoomInfo {
        private Long roomId;
        private String roomNumber;
        private String side;
        private TeacherInfo teacher;
    }

    @Data
    public static class TeacherInfo {
        private Long teacherId;
        private String name;
        private String serviceName;
    }

    @Data
    public static class ServiceInfo {
        private Long serviceId;
        private String name;
        private String description;
    }
}