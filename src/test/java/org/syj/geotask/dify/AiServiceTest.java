package org.syj.geotask.dify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;
import org.syj.geotask.dify.service.AiService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI服务测试类
 */
@SpringBootTest
public class AiServiceTest {
    
    @Autowired
    private AiService aiService;
    
    @Test
    public void testWorkflowService() {
        // 测试工作流功能
        java.util.Map<String, Object> inputs = new java.util.HashMap<>();
        inputs.put("task_name", "完成项目文档");
        inputs.put("priority", "high");
        inputs.put("deadline", "2024-01-15");
        
        WorkflowRequest request = new WorkflowRequest(inputs, "test-user");
        request.setWorkflowId("task-management-workflow");
        
        WorkflowResponse response = aiService.workflow(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getOutputs());
        assertNotNull(response.getExecutionId());
        assertEquals("completed", response.getStatus());
        
        System.out.println("工作流测试结果: " + response.getOutputs());
        System.out.println("执行ID: " + response.getExecutionId());
        System.out.println("耗时: " + response.getElapsedTime() + "ms");
    }
    
    @Test
    public void testWorkflowWithClient() {
        // 测试使用指定客户端执行工作流
        java.util.Map<String, Object> inputs = new java.util.HashMap<>();
        inputs.put("action", "analyze_priority");
        inputs.put("task_title", "系统性能优化");
        inputs.put("task_description", "优化数据库查询性能，提升系统响应速度");
        
        WorkflowRequest request = new WorkflowRequest(inputs, "test-user");
        request.setStreaming(false);
        
        WorkflowResponse response = aiService.workflowWithClient("DIFY", request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getOutputs());
        
        System.out.println("指定客户端工作流测试结果: " + response.getOutputs());
    }
    
    @Test
    public void testServiceAvailability() {
        // 测试服务可用性检查
        boolean isAvailable = aiService.isServiceAvailable();
        System.out.println("AI服务可用性: " + isAvailable);
        
        // 在模拟环境中，服务应该返回true
        assertTrue(isAvailable);
    }
}
