package org.syj.geotask.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.syj.geotask.task.entity.Task;
import org.syj.geotask.task.service.TaskService;

import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    /**
     * 创建任务
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        if (createdTask != null) {
            return ResponseEntity.ok(createdTask);
        }
        return ResponseEntity.badRequest().build();
    }
    
    /**
     * 根据ID删除任务
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long id) {
        boolean result = taskService.deleteTask(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新任务
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        Task updatedTask = taskService.updateTask(task);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 根据ID获取任务
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 获取所有任务
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 根据完成状态获取任务
     */
    @GetMapping("/completed/{isCompleted}")
    public ResponseEntity<List<Task>> getTasksByCompleted(@PathVariable Boolean isCompleted) {
        List<Task> tasks = taskService.getTasksByCompleted(isCompleted);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 根据标题搜索任务
     */
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByTitle(@RequestParam String title) {
        List<Task> tasks = taskService.searchTasksByTitle(title);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 获取指定时间范围内的任务
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Task>> getTasksByDateRange(
            @RequestParam Long startDate,
            @RequestParam Long endDate) {
        List<Task> tasks = taskService.getTasksByDateRange(startDate, endDate);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 获取指定地理位置范围内的任务
     */
    @GetMapping("/location")
    public ResponseEntity<List<Task>> getTasksByLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        List<Task> tasks = taskService.getTasksByLocation(latitude, longitude, radius);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 标记任务为已完成
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Boolean> markTaskAsCompleted(@PathVariable Long id) {
        boolean result = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 标记任务为未完成
     */
    @PutMapping("/{id}/uncomplete")
    public ResponseEntity<Boolean> markTaskAsUncompleted(@PathVariable Long id) {
        boolean result = taskService.markTaskAsUncompleted(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 切换任务完成状态
     */
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Boolean> toggleTaskCompletion(@PathVariable Long id) {
        boolean result = taskService.toggleTaskCompletion(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 启用任务提醒
     */
    @PutMapping("/{id}/enable-reminder")
    public ResponseEntity<Boolean> enableTaskReminder(@PathVariable Long id) {
        boolean result = taskService.enableTaskReminder(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 禁用任务提醒
     */
    @PutMapping("/{id}/disable-reminder")
    public ResponseEntity<Boolean> disableTaskReminder(@PathVariable Long id) {
        boolean result = taskService.disableTaskReminder(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取任务统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getTaskStatistics() {
        Long[] stats = taskService.getTaskStatistics();
        Map<String, Long> result = Map.of(
            "total", stats[0],
            "completed", stats[1],
            "uncompleted", stats[2]
        );
        return ResponseEntity.ok(result);
    }
}
