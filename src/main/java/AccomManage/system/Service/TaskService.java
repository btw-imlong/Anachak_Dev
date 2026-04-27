package AccomManage.system.Service;

import java.time.LocalDate;
import java.util.List;
import AccomManage.system.Dto.Request.CreateTaskRequest;
import AccomManage.system.Dto.Request.MarkTaskDoneRequest;
import AccomManage.system.Dto.Request.UpdateTaskRequest;
import AccomManage.system.Dto.Response.TaskCompletionResponse;
import AccomManage.system.Dto.Response.TaskResponse;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    void deleteTask(Long taskId);

    List<TaskResponse> getTasksByRoom(String roomNumber);
    List<TaskResponse> getTasksByRoomAndDay(String roomNumber, String dayOfWeek);
    List<TaskResponse> getTodayTasksByRoom(String roomNumber);

    TaskCompletionResponse markTaskDone(MarkTaskDoneRequest request);
    void unmarkTaskDone(Long taskId, LocalDate date);

    List<TaskCompletionResponse> getCompletionHistory(Long taskId);
    List<TaskCompletionResponse> getCompletionsByRoomAndDateRange(String roomNumber, LocalDate from, LocalDate to);
    List<TaskCompletionResponse> getAllCompletionsByDateRange(LocalDate from, LocalDate to);
}