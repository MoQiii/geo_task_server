package org.syj.geotask.task.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.syj.geotask.task.entity.Task;

import java.util.List;

/**
 * 任务数据访问接口
 */
@Mapper
public interface TaskMapper {
    
    /**
     * 插入任务
     * @param task 任务对象
     * @return 影响的行数
     */
    int insert(Task task);
    
    /**
     * 根据ID删除任务
     * @param id 任务ID
     * @return 影响的行数
     */
    int deleteById(Long id);
    
    /**
     * 根据ID更新任务
     * @param task 任务对象
     * @return 影响的行数
     */
    int updateById(Task task);
    
    /**
     * 根据ID查询任务
     * @param id 任务ID
     * @return 任务对象
     */
    Task selectById(Long id);
    
    /**
     * 查询所有任务
     * @return 任务列表
     */
    List<Task> selectAll();
    
    /**
     * 根据完成状态查询任务
     * @param isCompleted 是否已完成
     * @return 任务列表
     */
    List<Task> selectByCompleted(Boolean isCompleted);
    
    /**
     * 根据标题模糊查询任务
     * @param title 标题关键字
     * @return 任务列表
     */
    List<Task> selectByTitleLike(String title);
    
    /**
     * 查询指定时间范围内的任务
     * @param startDate 开始时间戳
     * @param endDate 结束时间戳
     * @return 任务列表
     */
    List<Task> selectByDateRange(Long startDate, Long endDate);
    
    /**
     * 查询指定地理位置范围内的任务
     * @param latitude 纬度
     * @param longitude 经度
     * @param radius 半径（米）
     * @return 任务列表
     */
    List<Task> selectByLocation(Double latitude, Double longitude, Double radius);
    
    /**
     * 统计任务总数
     * @return 任务总数
     */
    Long countTotal();
    
    /**
     * 统计已完成任务数
     * @return 已完成任务数
     */
    Long countCompleted();
    
    /**
     * 统计未完成任务数
     * @return 未完成任务数
     */
    Long countUncompleted();
}
