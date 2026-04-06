package AccomManage.system.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import AccomManage.system.Dto.Request.CreateTaskRequest;
import AccomManage.system.Dto.Request.MarkTaskDoneRequest;
import AccomManage.system.Dto.Request.UpdateTaskRequest;
import AccomManage.system.Dto.Response.TaskCompletionResponse;
import AccomManage.system.Dto.Response.TaskResponse;
import AccomManage.system.Service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping("/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getByRoom(@PathVariable String roomNumber) {
        return ResponseEntity.ok(taskService.getTasksByRoom(roomNumber));
    }

    @GetMapping("/room/{roomNumber}/day/{dayOfWeek}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getByRoomAndDay(
            @PathVariable String roomNumber,
            @PathVariable String dayOfWeek) {
        return ResponseEntity.ok(taskService.getTasksByRoomAndDay(roomNumber, dayOfWeek));
    }

    @GetMapping("/room/{roomNumber}/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getTodayTasks(@PathVariable String roomNumber) {
        return ResponseEntity.ok(taskService.getTodayTasksByRoom(roomNumber));
    }

    // Mark a task done for a specific date
    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<TaskCompletionResponse> markDone(
            @RequestBody MarkTaskDoneRequest request) {
        return ResponseEntity.ok(taskService.markTaskDone(request));
    }

    // Unmark a task done for a specific date
    @DeleteMapping("/{taskId}/complete/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> unmarkDone(
            @PathVariable Long taskId,
            @PathVariable String date) {
        taskService.unmarkTaskDone(taskId, LocalDate.parse(date));
        return ResponseEntity.ok("Task unmarked successfully");
    }

    // Get completion history for a single task
    @GetMapping("/{taskId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<TaskCompletionResponse>> history(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getCompletionHistory(taskId));
    }

    // Get completions for a room within a date range — used by frontend weekly grid
    @GetMapping("/completions/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskCompletionResponse>> getCompletionsByRoom(
            @PathVariable String roomNumber,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(
            taskService.getCompletionsByRoomAndDateRange(
                roomNumber, LocalDate.parse(from), LocalDate.parse(to)
            )
        );
    }

    // Get all completions across all rooms for a date range — used by admin overview
    @GetMapping("/completions")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<TaskCompletionResponse>> getAllCompletions(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(
            taskService.getAllCompletionsByDateRange(
                LocalDate.parse(from), LocalDate.parse(to)
            )
        );
    }
}