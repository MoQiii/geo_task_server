package org.syj.geotask.dify.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * 工作流请求模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequest {
    
    /**
     * 工作流输入参数
     */
    private Map<String, Object> inputs;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 工作流ID（可选）
     */
    private String workflowId;
    
    /**
     * 是否流式返回
     */
    private Boolean streaming = false;
    
    /**
     * 构造函数
     */
    public WorkflowRequest(Map<String, Object> inputs, String userId) {
        this.inputs = inputs;
        this.userId = userId;
        this.streaming = false;
    }
    
    /**
     * 添加输入参数
     */
    public WorkflowRequest addInput(String key, Object value) {
        if (this.inputs == null) {
            this.inputs = new java.util.HashMap<>();
        }
        this.inputs.put(key, value);
        return this;
    }
    
    /**
     * 设置流式模式
     */
    public WorkflowRequest enableStreaming() {
        this.streaming = true;
        return this;
    }
    
    /**
     * 设置阻塞模式
     */
    public WorkflowRequest enableBlocking() {
        this.streaming = false;
        return this;
    }
}
