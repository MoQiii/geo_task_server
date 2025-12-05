package org.syj.geotask.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syj.geotask.task.mapper.TaskMapper;
import org.syj.geotask.task.entity.Task;
import org.syj.geotask.task.service.TaskService;

import java.util.List;

/**
 * 任务服务实现类
 */
@Service
public class TaskServiceImpl implements TaskService {
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Override
    public Task createTask(Task task) {
        // 设置创建时间和更新时间
        task.setCreatedAt(System.currentTimeMillis());
        task.setUpdatedAt(System.currentTimeMillis());
        
        // 设置默认值
        if (task.getIsCompleted() == null) {
            task.setIsCompleted(false);
        }
        if (task.getIsReminderEnabled() == null) {
            task.setIsReminderEnabled(false);
        }
        if (task.getGeofenceRadius() == null) {
            task.setGeofenceRadius(200f);
        }
        if (task.getDescription() == null) {
            task.setDescription("");
        }
        
        int result = taskMapper.insert(task);
        if (result > 0) {
            return task;
        }
        return null;
    }
    
    @Override
    public boolean deleteTask(Long id) {
        if (id == null) {
            return false;
        }
        return taskMapper.deleteById(id) > 0;
    }
    
    @Override
    public Task updateTask(Task task) {
        if (task.getId() == null) {
            return null;
        }
        
        // 更新修改时间
        task.setUpdatedAt(System.currentTimeMillis());
        
        int result = taskMapper.updateById(task);
        if (result > 0) {
            return taskMapper.selectById(task.getId());
        }
        return null;
    }
    
    @Override
    public Task getTaskById(Long id) {
        if (id == null) {
            return null;
        }
        return taskMapper.selectById(id);
    }
    
    @Override
    public List<Task> getAllTasks() {
        return taskMapper.selectAll();
    }
    
    @Override
    public List<Task> getTasksByCompleted(Boolean isCompleted) {
        return taskMapper.selectByCompleted(isCompleted);
    }
    
    @Override
    public List<Task> searchTasksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllTasks();
        }
        return taskMapper.selectByTitleLike("%" + title.trim() + "%");
    }
    
    @Override
    public List<Task> getTasksByDateRange(Long startDate, Long endDate) {
        if (startDate == null || endDate == null) {
            return getAllTasks();
        }
        return taskMapper.selectByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Task> getTasksByLocation(Double latitude, Double longitude, Double radius) {
        if (latitude == null || longitude == null || radius == null) {
            return List.of();
        }
        return taskMapper.selectByLocation(latitude, longitude, radius);
    }
    
    @Override
    public boolean markTaskAsCompleted(Long id) {
        if (id == null) {
            return false;
        }
        
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return false;
        }
        
        task.setIsCompleted(true);
        task.setUpdatedAt(System.currentTimeMillis());
        return taskMapper.updateById(task) > 0;
    }
    
    @Override
    public boolean markTaskAsUncompleted(Long id) {
        if (id == null) {
            return false;
        }
        
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return false;
        }
        
        task.setIsCompleted(false);
        task.setUpdatedAt(System.currentTimeMillis());
        return taskMapper.updateById(task) > 0;
    }
    
    @Override
    public boolean toggleTaskCompletion(Long id) {
        if (id == null) {
            return false;
        }
        
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return false;
        }
        
        task.setIsCompleted(!task.getIsCompleted());
        task.setUpdatedAt(System.currentTimeMillis());
        return taskMapper.updateById(task) > 0;
    }
    
    @Override
    public boolean enableTaskReminder(Long id) {
        if (id == null) {
            return false;
        }
        
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return false;
        }
        
        task.setIsReminderEnabled(true);
        task.setUpdatedAt(System.currentTimeMillis());
        return taskMapper.updateById(task) > 0;
    }
    
    @Override
    public boolean disableTaskReminder(Long id) {
        if (id == null) {
            return false;
        }
        
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return false;
        }
        
        task.setIsReminderEnabled(false);
        task.setUpdatedAt(System.currentTimeMillis());
        return taskMapper.updateById(task) > 0;
    }
    
    @Override
    public Long[] getTaskStatistics() {
        Long total = taskMapper.countTotal();
        Long completed = taskMapper.countCompleted();
        Long uncompleted = taskMapper.countUncompleted();
        
        return new Long[]{total, completed, uncompleted};
    }
}
