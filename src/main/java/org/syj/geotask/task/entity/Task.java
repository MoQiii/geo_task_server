package org.syj.geotask.task.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 任务实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 任务标题
     */
    private String title;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 截止日期（时间戳）
     */
    private Long dueDate;
    
    /**
     * 截止时间（时间戳）
     */
    private Long dueTime;
    
    /**
     * 是否已完成
     */
    private Boolean isCompleted;
    
    /**
     * 是否启用提醒
     */
    private Boolean isReminderEnabled;
    
    /**
     * 地址描述
     */
    private String location;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 地理围栏半径（米）
     */
    private Float geofenceRadius;
    
    /**
     * 创建时间（时间戳）
     */
    private Long createdAt;
    
    /**
     * 更新时间（时间戳）
     */
    private Long updatedAt;
    
    /**
     * 构造函数（创建时使用）
     */
    public Task(String title, String description, Long dueDate, Long dueTime) {
        this.title = title;
        this.description = description != null ? description : "";
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.isCompleted = false;
        this.isReminderEnabled = false;
        this.geofenceRadius = 200f;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
}
