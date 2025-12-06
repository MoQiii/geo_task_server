package org.syj.geotask.dify.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syj.geotask.dify.client.AiClient;
import org.syj.geotask.dify.client.AiClientFactory;
import org.syj.geotask.dify.config.DifyConfig;
import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;
import org.syj.geotask.dify.service.AiService;

import java.util.HashMap;
import java.util.Map;

/**
 * AI服务实现类
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {
    
    @Autowired
    private AiClientFactory aiClientFactory;
    
    @Autowired
    private DifyConfig difyConfig;

    
    @Override
    public WorkflowResponse workflow(WorkflowRequest request) {
        try {
            AiClient aiClient = aiClientFactory.getDefaultClient();
            
            // 设置默认用户ID
            if (request.getUserId() == null) {
                request.setUserId(difyConfig.getDefaultUserId());
            }
            
            return aiClient.workflow(request);
        } catch (Exception e) {
            log.error("工作流服务调用失败", e);
            return new WorkflowResponse(false, "AI服务当前不可用: " + e.getMessage());
        }
    }
    
    @Override
    public WorkflowResponse workflowWithClient(String clientType, WorkflowRequest request) {
        try {
            AiClient aiClient = aiClientFactory.getClient(clientType);
            
            // 设置默认用户ID
            if (request.getUserId() == null) {
                request.setUserId(difyConfig.getDefaultUserId());
            }
            
            return aiClient.workflow(request);
        } catch (Exception e) {
            log.error("使用指定客户端{}执行工作流失败", clientType, e);
            return new WorkflowResponse(false, "AI服务不可用: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isServiceAvailable() {
        try {
            return !aiClientFactory.getAvailableClients().isEmpty();
        } catch (Exception e) {
            log.error("检查AI服务可用性失败", e);
            return false;
        }
    }
    
    @Override
    public Map<String, Boolean> getClientStatus() {
        Map<String, Boolean> status = new HashMap<>();
        for (String clientType : aiClientFactory.getRegisteredClientTypes()) {
            status.put(clientType, aiClientFactory.isClientAvailable(clientType));
        }
        return status;
    }
    
    @Override
    public String getDefaultClientType() {
        try {
            return aiClientFactory.getDefaultClient().getClientType();
        } catch (Exception e) {
            log.error("获取默认客户端类型失败", e);
            return null;
        }
    }
}
