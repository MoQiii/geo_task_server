package org.syj.geotask.dify.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * 工作流响应模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowResponse {
    
    /**
     * 工作流执行结果
     */
    private Map<String, Object> outputs;
    
    /**
     * 工作流ID
     */
    private String workflowId;
    
    /**
     * 执行ID
     */
    private String executionId;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 执行状态
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private Long createdAt;
    
    /**
     * 完成时间
     */
    private Long finishedAt;
    
    /**
     * 总耗时（毫秒）
     */
    private Long elapsedTime;
    
    /**
     * 总Token消耗
     */
    private Integer totalTokens;
    
    /**
     * 是否成功
     */
    private Boolean success;
    
    /**
     * 额外元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 构造函数（成功情况）
     */
    public WorkflowResponse(Map<String, Object> outputs, Boolean success) {
        this.outputs = outputs;
        this.success = success;
        this.createdAt = System.currentTimeMillis();
        this.finishedAt = System.currentTimeMillis();
        if (this.createdAt != null && this.finishedAt != null) {
            this.elapsedTime = this.finishedAt - this.createdAt;
        }
    }
    
    /**
     * 构造函数（错误情况）
     */
    public WorkflowResponse(Boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.createdAt = System.currentTimeMillis();
        this.finishedAt = System.currentTimeMillis();
    }
    
    /**
     * 添加输出结果
     */
    public WorkflowResponse addOutput(String key, Object value) {
        if (this.outputs == null) {
            this.outputs = new java.util.HashMap<>();
        }
        this.outputs.put(key, value);
        return this;
    }
    
    /**
     * 设置执行状态
     */
    public WorkflowResponse setStatus(String status) {
        this.status = status;
        return this;
    }
    
    /**
     * 标记为完成
     */
    public WorkflowResponse markCompleted() {
        this.status = "completed";
        this.finishedAt = System.currentTimeMillis();
        if (this.createdAt != null) {
            this.elapsedTime = this.finishedAt - this.createdAt;
        }
        return this;
    }
    
    /**
     * 标记为失败
     */
    public WorkflowResponse markFailed(String errorMessage) {
        this.status = "failed";
        this.success = false;
        this.errorMessage = errorMessage;
        this.finishedAt = System.currentTimeMillis();
        if (this.createdAt != null) {
            this.elapsedTime = this.finishedAt - this.createdAt;
        }
        return this;
    }
}
