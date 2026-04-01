package AccomManage.system.Controller;

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

    // 🔐 Admin + Teacher — create task
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    // 🔐 Admin + Teacher — update task schedule
    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    // 🔐 Admin + Teacher — delete task
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    // 🔐 All roles — get all tasks for a room
    @GetMapping("/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getByRoom(@PathVariable String roomNumber) {
        return ResponseEntity.ok(taskService.getTasksByRoom(roomNumber));
    }

    // 🔐 All roles — get tasks for a room on a specific day
    @GetMapping("/room/{roomNumber}/day/{dayOfWeek}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getByRoomAndDay(
            @PathVariable String roomNumber,
            @PathVariable String dayOfWeek) {
        return ResponseEntity.ok(taskService.getTasksByRoomAndDay(roomNumber, dayOfWeek));
    }

    // 🔐 All roles — get today's tasks for a room
    @GetMapping("/room/{roomNumber}/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<TaskResponse>> getTodayTasks(@PathVariable String roomNumber) {
        return ResponseEntity.ok(taskService.getTodayTasksByRoom(roomNumber));
    }

    // 🔐 Teacher + Student — mark task as done
    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<TaskCompletionResponse> markDone(@RequestBody MarkTaskDoneRequest request) {
        return ResponseEntity.ok(taskService.markTaskDone(request));
    }

    // 🔐 Admin + Teacher — view completion history
    @GetMapping("/{taskId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<TaskCompletionResponse>> history(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getCompletionHistory(taskId));
    }
}