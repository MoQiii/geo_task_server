package org.syj.geotask.dify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.syj.geotask.dify.model.WorkflowRequest;
import org.syj.geotask.dify.model.WorkflowResponse;
import org.syj.geotask.dify.service.AiService;

import java.util.HashMap;
import java.util.Map;

/**
 * AI功能控制器
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {
    
    @Autowired
    private AiService aiService;

    /**
     * 执行工作流（使用默认客户端）
     */
    @PostMapping("/workflow")
    public ResponseEntity<WorkflowResponse> workflow(@RequestBody WorkflowRequest request) {
        WorkflowResponse response = aiService.workflow(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 使用指定客户端执行工作流
     */
    @PostMapping("/workflow/{clientType}")
    public ResponseEntity<WorkflowResponse> workflowWithClient(
            @PathVariable String clientType,
            @RequestBody WorkflowRequest request) {
        WorkflowResponse response = aiService.workflowWithClient(clientType, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 检查AI服务状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("available", aiService.isServiceAvailable());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取所有客户端状态
     */
    @GetMapping("/clients/status")
    public ResponseEntity<Map<String, Object>> getClientsStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("clients", aiService.getClientStatus());
        response.put("defaultClient", aiService.getDefaultClientType());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

}
