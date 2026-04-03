package AccomManage.system.Service.Impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import AccomManage.system.Dto.Request.CreateTaskRequest;
import AccomManage.system.Dto.Request.MarkTaskDoneRequest;
import AccomManage.system.Dto.Request.UpdateTaskRequest;
import AccomManage.system.Dto.Response.TaskCompletionResponse;
import AccomManage.system.Dto.Response.TaskResponse;
import AccomManage.system.Entity.Room;
import AccomManage.system.Entity.Task;
import AccomManage.system.Entity.TaskCompletion;
import AccomManage.system.Entity.TaskStatus;
import AccomManage.system.Repositories.RoomRepository;
import AccomManage.system.Repositories.TaskCompletionRepository;
import AccomManage.system.Repositories.TaskRepository;
import AccomManage.system.Repositories.UserRepository;
import AccomManage.system.Service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired private TaskRepository taskRepo;
    @Autowired private TaskCompletionRepository completionRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private UserRepository userRepo;

    // ✅ Create task for a room
    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Room room = roomRepo.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        // ✅ Rule 1: same room + same day + same time = duplicate
        boolean sameRoomDuplicate = taskRepo.existsByRoomIdAndDayOfWeekAndTaskTime(
                room.getId(),
                parseDayOfWeek(request.getDayOfWeek()),
                request.getTaskTime()
        );
        if (sameRoomDuplicate)
            throw new RuntimeException(
                "A task already exists in room " + request.getRoomNumber() +
                " on " + request.getDayOfWeek() +
                " at " + request.getTaskTime()
            );

        // ✅ Rule 2: same side + same title + same time = duplicate
        boolean sameSideDuplicate = taskRepo.existsDuplicateOnSameSide(
                room.getSide(),
                parseDayOfWeek(request.getDayOfWeek()),
                request.getTitle(),
                request.getTaskTime(),
                room.getId()
        );
        if (sameSideDuplicate)
            throw new RuntimeException(
                "A task with the same title and time already exists " +
                "in another room on the " + room.getSide() + " side on " +
                request.getDayOfWeek()
            );

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDayOfWeek(parseDayOfWeek(request.getDayOfWeek()));
        task.setTaskTime(request.getTaskTime());
        task.setStatus(TaskStatus.PENDING);
        task.setRoom(room);

        return mapToTaskResponse(taskRepo.save(task));
    }
    // ✅ Update task (change schedule)
    @Override
public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

    DayOfWeek newDay = request.getDayOfWeek() != null
            ? parseDayOfWeek(request.getDayOfWeek())
            : task.getDayOfWeek();

    java.time.LocalTime newTime = request.getTaskTime() != null
            ? request.getTaskTime()
            : task.getTaskTime();

    String newTitle = request.getTitle() != null
            ? request.getTitle()
            : task.getTitle();

    // ✅ Rule 1: same room + same day + same time (exclude self)
    boolean sameRoomDuplicate = taskRepo
            .existsByRoomIdAndDayOfWeekAndTaskTimeAndIdNot(
                    task.getRoom().getId(), newDay, newTime, taskId);
    if (sameRoomDuplicate)
        throw new RuntimeException(
            "A task already exists in this room on " +
            newDay + " at " + newTime
        );

    // ✅ Rule 2: same side + same title + same time (exclude self)
    boolean sameSideDuplicate = taskRepo.existsDuplicateOnSameSide(
            task.getRoom().getSide(), newDay, newTitle, newTime, task.getRoom().getId());
    if (sameSideDuplicate)
        throw new RuntimeException(
            "A task with the same title and time already exists " +
            "on the " + task.getRoom().getSide() + " side on " + newDay
        );

    if (request.getTitle() != null) task.setTitle(newTitle);
    if (request.getDescription() != null) task.setDescription(request.getDescription());
    if (request.getDayOfWeek() != null) task.setDayOfWeek(newDay);
    if (request.getTaskTime() != null) task.setTaskTime(newTime);
    task.setStatus(TaskStatus.PENDING);

    return mapToTaskResponse(taskRepo.save(task));
}

    // ✅ Delete task
    @Override
    public void deleteTask(Long taskId) {
        if (!taskRepo.existsById(taskId))
            throw new RuntimeException("Task not found with id: " + taskId);

        // delete completion history first
        List<TaskCompletion> completions = completionRepo.findByTaskId(taskId);
        completionRepo.deleteAll(completions);

        taskRepo.deleteById(taskId);
    }

    // ✅ Get all tasks for a room
    @Override
    public List<TaskResponse> getTasksByRoom(String roomNumber) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
        return taskRepo.findByRoomId(room.getId())
                .stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    // ✅ Get tasks for a room on a specific day
    @Override
    public List<TaskResponse> getTasksByRoomAndDay(String roomNumber, String dayOfWeek) {
        Room room = roomRepo.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
        return taskRepo.findByRoomIdAndDayOfWeek(room.getId(), parseDayOfWeek(dayOfWeek))
                .stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    // ✅ Get today's tasks for a room
    @Override
    public List<TaskResponse> getTodayTasksByRoom(String roomNumber) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return getTasksByRoomAndDay(roomNumber, today.name());
    }

    // ✅ Mark task as done (teacher or student)
    @Override
    public TaskCompletionResponse markTaskDone(MarkTaskDoneRequest request) {
        Task task = taskRepo.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + request.getTaskId()));

        // check if already marked done for this date
        if (completionRepo.existsByTaskIdAndCompletedDate(request.getTaskId(), request.getCompletedDate()))
            throw new RuntimeException("Task already marked as done for this date");

        // get current logged in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskCompletion completion = new TaskCompletion();
        completion.setTask(task);
        completion.setCompletedDate(request.getCompletedDate());
        completion.setMarkedAt(LocalDateTime.now());
        completion.setMarkedByRole(user.getRole().name());
        completion.setMarkedByName(user.getName());

        // update task status
        task.setStatus(TaskStatus.DONE);
        taskRepo.save(task);

        return mapToCompletionResponse(completionRepo.save(completion));
    }

    // ✅ Get completion history for a task
    @Override
    public List<TaskCompletionResponse> getCompletionHistory(Long taskId) {
        if (!taskRepo.existsById(taskId))
            throw new RuntimeException("Task not found with id: " + taskId);
        return completionRepo.findByTaskId(taskId)
                .stream().map(this::mapToCompletionResponse).collect(Collectors.toList());
    }

    // ✅ Helpers
    private DayOfWeek parseDayOfWeek(String day) {
        try {
            return DayOfWeek.valueOf(day.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid day: " + day + ". Use MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY");
        }
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setTaskId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        res.setDayOfWeek(task.getDayOfWeek().name());
        res.setTaskTime(task.getTaskTime().toString());
        res.setStatus(task.getStatus().name());
        res.setRoomNumber(task.getRoom().getRoomNumber());
        res.setSide(task.getRoom().getSide());
        return res;
    }

    private TaskCompletionResponse mapToCompletionResponse(TaskCompletion c) {
        TaskCompletionResponse res = new TaskCompletionResponse();
        res.setCompletionId(c.getId());
        res.setTaskId(c.getTask().getId());
        res.setTaskTitle(c.getTask().getTitle());
        res.setCompletedDate(c.getCompletedDate().toString());
        res.setMarkedAt(c.getMarkedAt().toString());
        res.setMarkedByRole(c.getMarkedByRole());
        res.setMarkedByName(c.getMarkedByName());
        return res;
    }
}