package org.syj.geotask.task.service;

import org.syj.geotask.task.entity.Task;

import java.util.List;

/**
 * 任务服务接口
 */
public interface TaskService {
    
    /**
     * 创建任务
     * @param task 任务对象
     * @return 创建的任务对象
     */
    Task createTask(Task task);
    
    /**
     * 根据ID删除任务
     * @param id 任务ID
     * @return 是否删除成功
     */
    boolean deleteTask(Long id);
    
    /**
     * 更新任务
     * @param task 任务对象
     * @return 更新后的任务对象
     */
    Task updateTask(Task task);
    
    /**
     * 根据ID获取任务
     * @param id 任务ID
     * @return 任务对象
     */
    Task getTaskById(Long id);
    
    /**
     * 获取所有任务
     * @return 任务列表
     */
    List<Task> getAllTasks();
    
    /**
     * 根据完成状态获取任务
     * @param isCompleted 是否已完成
     * @return 任务列表
     */
    List<Task> getTasksByCompleted(Boolean isCompleted);
    
    /**
     * 根据标题搜索任务
     * @param title 标题关键字
     * @return 任务列表
     */
    List<Task> searchTasksByTitle(String title);
    
    /**
     * 获取指定时间范围内的任务
     * @param startDate 开始时间戳
     * @param endDate 结束时间戳
     * @return 任务列表
     */
    List<Task> getTasksByDateRange(Long startDate, Long endDate);
    
    /**
     * 获取指定地理位置范围内的任务
     * @param latitude 纬度
     * @param longitude 经度
     * @param radius 半径（米）
     * @return 任务列表
     */
    List<Task> getTasksByLocation(Double latitude, Double longitude, Double radius);
    
    /**
     * 标记任务为已完成
     * @param id 任务ID
     * @return 是否标记成功
     */
    boolean markTaskAsCompleted(Long id);
    
    /**
     * 标记任务为未完成
     * @param id 任务ID
     * @return 是否标记成功
     */
    boolean markTaskAsUncompleted(Long id);
    
    /**
     * 切换任务完成状态
     * @param id 任务ID
     * @return 切换后的完成状态
     */
    boolean toggleTaskCompletion(Long id);
    
    /**
     * 启用任务提醒
     * @param id 任务ID
     * @return 是否启用成功
     */
    boolean enableTaskReminder(Long id);
    
    /**
     * 禁用任务提醒
     * @param id 任务ID
     * @return 是否禁用成功
     */
    boolean disableTaskReminder(Long id);
    
    /**
     * 获取任务统计信息
     * @return 统计信息数组 [总数, 已完成数, 未完成数]
     */
    Long[] getTaskStatistics();
}
