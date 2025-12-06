package org.syj.geotask.dify.client;

import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;

/**
 * AI客户端抽象接口
 * 定义了与AI服务交互的基本方法
 */
public interface AiClient {

    /**
     * 执行工作流
     * @param request 工作流请求
     * @return 工作流响应
     */
    WorkflowResponse workflow(WorkflowRequest request);
    
    /**
     * 检查客户端是否可用
     * @return 是否可用
     */
    boolean isAvailable();
    
    /**
     * 获取客户端类型
     * @return 客户端类型
     */
    String getClientType();
}
