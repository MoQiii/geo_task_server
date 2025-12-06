package org.syj.geotask.dify.service;

import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;

import java.util.Map;

/**
 * AI服务接口
 * 提供AI相关的高级服务
 */
public interface AiService {

    
    /**
     * 执行工作流（使用默认客户端）
     * @param request 工作流请求
     * @return 工作流响应
     */
    WorkflowResponse workflow(WorkflowRequest request);
    
    /**
     * 使用指定客户端执行工作流
     * @param clientType 客户端类型
     * @param request 工作流请求
     * @return 工作流响应
     */
    WorkflowResponse workflowWithClient(String clientType, WorkflowRequest request);

    /**
     * 检查AI服务可用性
     * @return 是否可用
     */
    boolean isServiceAvailable();
    
    /**
     * 获取所有客户端的状态
     * @return 客户端状态映射（客户端类型 -> 是否可用）
     */
    Map<String, Boolean> getClientStatus();
    
    /**
     * 获取默认客户端类型
     * @return 默认客户端类型
     */
    String getDefaultClientType();
}
