package AccomManage.system.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.AttendanceRecord;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
	 List<AttendanceRecord> findByAttendanceId(Long attendanceId);
	 boolean existsByStudentIdAndAttendanceDate(Long studentId, LocalDate date);
	// Add to AttendanceRecordRepository.java
	 List<AttendanceRecord> findByStudentId(Long studentId);

	 List<AttendanceRecord> findByStudentIdAndAttendance_DateBetween(
	     Long studentId, LocalDate from, LocalDate to);
}
