package org.syj.geotask.dify.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.syj.geotask.dify.client.AiClient;
import org.syj.geotask.dify.config.DifyConfig;
import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.github.yuanbaobaoo.dify.app.IAppFlowClient;
import io.github.yuanbaobaoo.dify.app.params.ParamMessage;
import io.github.yuanbaobaoo.dify.app.types.DifyWorkFlowResult;
import io.github.yuanbaobaoo.dify.types.DifyException;
import io.github.yuanbaobaoo.dify.utils.AppClientBuilder;
import com.alibaba.fastjson2.JSONObject;

/**
 * Dify AI客户端实现
 * 基于dify-java-client实现具体的AI服务调用
 */
@Slf4j
@Component
public class DifyAiClient implements AiClient {
    
    private static final String CLIENT_TYPE = "DIFY";
    
    private final DifyConfig difyConfig;
    private final IAppFlowClient flowClient;
    
    @Autowired
    public DifyAiClient(DifyConfig difyConfig) {
        this.difyConfig = difyConfig;
        
        // 初始化dify工作流客户端
        try {
            this.flowClient = AppClientBuilder.builder(
                difyConfig.getBaseUrl(),
                difyConfig.getAppApiKey()
            ).flow().build();
                
            log.info("Dify AI客户端初始化完成，基础URL: {}", difyConfig.getBaseUrl());
        } catch (Exception e) {
            log.error("Dify AI客户端初始化失败", e);
            throw new RuntimeException("Dify AI客户端初始化失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowResponse workflow(WorkflowRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("执行工作流请求，输入参数: {}, 用户ID: {}, 流式模式: {}", 
                request.getInputs(), request.getUserId(), request.getStreaming());
            
            // 构建dify工作流请求参数
            ParamMessage paramMessage = ParamMessage.builder()
                .inputs(request.getInputs())
                .user(request.getUserId() != null ? request.getUserId() : difyConfig.getDefaultUserId())
                .autoGenerateName(true)
                .build();
            
            JSONObject difyResponse;
            
            if (request.getStreaming()) {
                // 流式响应处理
                log.info("使用流式模式调用工作流");
                difyResponse = handleStreamingWorkflow(paramMessage);
            } else {
                // 阻塞式响应处理
                log.info("使用阻塞模式调用工作流");
                difyResponse = flowClient.runBlocking(paramMessage);
            }
            
            // 转换为我们的响应模型
            WorkflowResponse response = convertToWorkflowResponse(difyResponse, startTime);
            
            log.info("工作流执行完成，执行ID: {}, 状态: {}, 耗时: {}ms", 
                response.getExecutionId(), response.getStatus(), response.getElapsedTime());
            
            return response;
            
        } catch (DifyException e) {
            log.error("Dify API调用失败", e);
            return createErrorResponse(e.getMessage(), startTime);
        } catch (Exception e) {
            log.error("工作流执行失败", e);
            return createErrorResponse("工作流执行失败: " + e.getMessage(), startTime);
        }
    }
    
    /**
     * 处理流式工作流响应
     */
    private JSONObject handleStreamingWorkflow(ParamMessage paramMessage) {
        try {
            AtomicReference<JSONObject> finalResult = new AtomicReference<>();
            CompletableFuture<Void> future = new CompletableFuture<>();
            
            // 使用流式API调用工作流
            flowClient.runStreaming(paramMessage, result -> {
                try {
                    log.debug("收到流式响应，事件: {}", result.getEvent());
                    
                    if (result.getPayload() != null) {
                        finalResult.set(result.getPayload());
                        
                        // 检查是否完成
                        String status = result.getPayload().getString("status");
                        if ("succeeded".equals(status) || "failed".equals(status)) {
                            future.complete(null);
                        }
                    }
                } catch (Exception e) {
                    log.error("处理流式响应失败", e);
                    future.completeExceptionally(e);
                }
            });
            
            // 等待流式处理完成，设置超时时间
            try {
                future.get(difyConfig.getReadTimeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("流式处理超时或失败", e);
                future.cancel(true);
            }
            
            JSONObject result = finalResult.get();
            if (result == null) {
                // 如果没有收到有效响应，创建错误响应
                result = new JSONObject();
                result.put("status", "failed");
                result.put("error", "流式响应处理失败：未收到有效响应");
            }
            
            return result;
                
        } catch (Exception e) {
            log.error("流式工作流处理失败", e);
            JSONObject errorResult = new JSONObject();
            errorResult.put("status", "failed");
            errorResult.put("error", "流式响应处理失败: " + e.getMessage());
            return errorResult;
        }
    }
    
    /**
     * 将Dify响应转换为我们的响应模型
     * 根据新的difyResponse结构进行转换：
     * {
     *     "task_id": "...",
     *     "workflow_run_id": "abccd5f0-c870-4b4a-a94d-f2d0b6f18822",
     *     "data": {
     *         "id": "abccd5f0-c870-4b4a-a94d-f2d0b6f18822",
     *         "workflow_id": "3a69c457-6339-48ed-b878-2ec77f91e284",
     *         "status": "succeeded",
     *         "outputs": {...},
     *         "error": "",
     *         "elapsed_time": "9.220046",
     *         "total_tokens": 2628,
     *         "total_steps": 4,
     *         "created_at": 1765014899,
     *         "finished_at": 1765014909
     *     }
     * }
     */
    private WorkflowResponse convertToWorkflowResponse(JSONObject difyResponse, long startTime) {
        WorkflowResponse response = new WorkflowResponse();
        
        try {
            // 从顶层获取task_id和workflow_run_id
            response.setTaskId(difyResponse.getString("task_id"));
            String workflowRunId = difyResponse.getString("workflow_run_id");
            
            // 获取data对象
            JSONObject data = difyResponse.getJSONObject("data");
            if (data == null) {
                log.warn("Dify响应中缺少data字段");
                return createErrorResponse("响应格式错误：缺少data字段", startTime);
            }
            
            // 基本信息
            response.setExecutionId(data.getString("id"));
            response.setWorkflowId(data.getString("workflow_id"));
            response.setStatus(data.getString("status"));
            
            // 如果executionId为空，使用workflow_run_id
            if (response.getExecutionId() == null && workflowRunId != null) {
                response.setExecutionId(workflowRunId);
            }
            
            // 输出结果
            JSONObject outputs = data.getJSONObject("outputs");
            if (outputs != null) {
                Map<String, Object> outputMap = new HashMap<>();
                outputs.forEach(outputMap::put);
                response.setOutputs(outputMap);
            }
            
            // 时间信息 - 使用data中的时间戳
            Long createdAt = data.getLong("created_at");
            Long finishedAt = data.getLong("finished_at");
            
            if (createdAt != null) {
                response.setCreatedAt(createdAt * 1000); // 转换为毫秒
            } else {
                response.setCreatedAt(startTime);
            }
            
            if (finishedAt != null) {
                response.setFinishedAt(finishedAt * 1000); // 转换为毫秒
            } else {
                response.setFinishedAt(System.currentTimeMillis());
            }
            
            // 计算耗时 - 优先使用data中的elapsed_time
            String elapsedTimeStr = data.getString("elapsed_time");
            if (elapsedTimeStr != null && !elapsedTimeStr.isEmpty()) {
                try {
                    // elapsed_time是秒为单位的小数，转换为毫秒
                    double elapsedTimeSeconds = Double.parseDouble(elapsedTimeStr);
                    response.setElapsedTime((long) (elapsedTimeSeconds * 1000));
                } catch (NumberFormatException e) {
                    log.warn("解析elapsed_time失败: {}", elapsedTimeStr);
                    response.setElapsedTime(response.getFinishedAt() - response.getCreatedAt());
                }
            } else {
                response.setElapsedTime(response.getFinishedAt() - response.getCreatedAt());
            }
            
            // 成功状态
            String status = data.getString("status");
            response.setSuccess("succeeded".equals(status) || "finished".equals(status));
            
            // 错误信息
            String error = data.getString("error");
            if (error != null && !error.isEmpty()) {
                response.setErrorMessage(error);
                response.setSuccess(false);
            }
            
            // Token使用情况
            Integer totalTokens = data.getInteger("total_tokens");
            if (totalTokens != null) {
                response.setTotalTokens(totalTokens);
            }
            
            // 其他元数据
            Map<String, Object> metadataMap = new HashMap<>();
            
            // 添加total_steps到元数据
            Integer totalSteps = data.getInteger("total_steps");
            if (totalSteps != null) {
                metadataMap.put("total_steps", totalSteps);
            }
            
            // 添加elapsed_time原始值到元数据
            if (elapsedTimeStr != null) {
                metadataMap.put("elapsed_time_original", elapsedTimeStr);
            }
            
            // 添加workflow_run_id到元数据
            if (workflowRunId != null) {
                metadataMap.put("workflow_run_id", workflowRunId);
            }
            
            if (!metadataMap.isEmpty()) {
                response.setMetadata(metadataMap);
            }
            
            log.debug("成功转换Dify响应，执行ID: {}, 状态: {}, 耗时: {}ms", 
                response.getExecutionId(), response.getStatus(), response.getElapsedTime());
            
        } catch (Exception e) {
            log.error("转换Dify响应失败", e);
            return createErrorResponse("响应转换失败: " + e.getMessage(), startTime);
        }
        
        return response;
    }
    
    /**
     * 创建错误响应
     */
    private WorkflowResponse createErrorResponse(String errorMessage, long startTime) {
        WorkflowResponse response = new WorkflowResponse();
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        response.setStatus("failed");
        response.setCreatedAt(startTime);
        response.setFinishedAt(System.currentTimeMillis());
        response.setElapsedTime(response.getFinishedAt() - response.getCreatedAt());
        return response;
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // 检查配置是否完整
            if (difyConfig.getBaseUrl() == null || difyConfig.getBaseUrl().isEmpty() ||
                difyConfig.getAppApiKey() == null || difyConfig.getAppApiKey().isEmpty()) {
                log.warn("Dify配置不完整，基础URL: {}, API密钥: {}", 
                    difyConfig.getBaseUrl(), 
                    difyConfig.getAppApiKey() != null ? "***已配置***" : "***未配置***");
                return false;
            }
            
            // 检查客户端是否已初始化
            if (flowClient == null) {
                log.warn("Dify客户端未初始化");
                return false;
            }
            
            // 可以通过发送一个简单的健康检查请求来验证服务可用性
            // 这里简化为检查配置和客户端状态
            return true;
            
        } catch (Exception e) {
            log.error("检查Dify服务可用性失败", e);
            return false;
        }
    }
    
    @Override
    public String getClientType() {
        return CLIENT_TYPE;
    }
}
