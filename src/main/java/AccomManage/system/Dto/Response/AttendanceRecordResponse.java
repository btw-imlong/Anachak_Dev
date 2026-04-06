package AccomManage.system.Dto.Response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceRecordResponse {
    private Long recordId;      // AttendanceRecord id
    private Long studentId;
    private String studentName;
    private String status;   
    private String teacherName;// PRESENT, ABSENT, LATE
    private LocalDate date;
}