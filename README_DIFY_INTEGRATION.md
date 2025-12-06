# Dify Java Client 集成指南

本项目成功集成了 dify-java-client，提供了完整的 AI 服务抽象层和具体实现。

## 项目结构

```
src/main/java/org/syj/geotask/dify/
├── client/
│   ├── AiClient.java                    # AI客户端抽象接口
│   ├── AiClientFactory.java             # 客户端工厂
│   └── impl/
│       └── DifyAiClient.java           # Dify客户端实现
├── config/
│   └── DifyConfig.java                  # Dify配置类
├── controller/
│   └── AiController.java                # REST API控制器
├── model/
│   ├── ChatRequest.java                 # 聊天请求模型
│   ├── ChatResponse.java                # 聊天响应模型
│   ├── CompletionRequest.java           # 文本完成请求模型
│   ├── CompletionResponse.java          # 文本完成响应模型
│   ├── WorkflowRequest.java             # 工作流请求模型
│   └── WorkflowResponse.java            # 工作流响应模型
└── service/
    ├── AiService.java                   # AI服务接口
    └── impl/
        └── AiServiceImpl.java           # AI服务实现
```

## 核心组件

### 1. AiClient 抽象接口

定义了统一的AI客户端接口，支持：
- `chat(ChatRequest)` - 聊天对话
- `completion(CompletionRequest)` - 文本完成
- `isAvailable()` - 检查服务可用性
- `getClientType()` - 获取客户端类型

### 2. DifyAiClient 具体实现

基于 dify-java-client 的具体实现，当前使用模拟响应，预留了真实API调用的代码结构。

### 3. AiClientFactory 工厂模式

支持多种AI客户端的创建和管理，当前支持：
- DIFY 类型客户端
- 可扩展支持其他AI服务提供商

### 4. 统一模型层

- `ChatRequest/ChatResponse` - 聊天相关模型
- `CompletionRequest/CompletionResponse` - 文本完成相关模型
- `WorkflowRequest/WorkflowResponse` - 工作流相关模型
- 统一的错误处理和元数据支持

## 配置说明

在 `application.properties` 中配置：

```properties
# Dify配置
dify.base-url=https://api.dify.ai/v1
dify.app-api-key=your-app-api-key
dify.default-user-id=default-user
dify.enabled=true
```

## API 接口

### 聊天接口
```
POST /api/ai/chat
Content-Type: application/json

{
  "message": "你好，请介绍一下自己",
  "userId": "user123",
  "conversationId": "conv123",
  "variables": {
    "key": "value"
  }
}
```

### 文本完成接口
```
POST /api/ai/completion
Content-Type: application/json

{
  "prompt": "请写一段关于春天的描述",
  "userId": "user123",
  "variables": {
    "style": "poetic"
  }
}
```

### 工作流接口
```
POST /api/ai/workflow
Content-Type: application/json

{
  "inputs": {
    "task_name": "完成项目文档",
    "priority": "high",
    "deadline": "2024-01-15"
  },
  "userId": "user123",
  "workflowId": "task-management-workflow",
  "streaming": false
}
```

### 简化工作流接口
```
POST /api/ai/simple-workflow
Content-Type: application/x-www-form-urlencoded

inputs[task_name]=完成项目文档&inputs[priority]=high&userId=user123&workflowId=task-workflow
```

### 健康检查接口
```
GET /api/ai/health
```

## 使用示例

### 1. 通过服务层使用

```java
@Autowired
private AiService aiService;

// 聊天
ChatRequest chatRequest = new ChatRequest();
chatRequest.setMessage("你好");
ChatResponse chatResponse = aiService.chat(chatRequest);

// 文本完成
CompletionRequest completionRequest = new CompletionRequest();
completionRequest.setPrompt("请写一首诗");
CompletionResponse completionResponse = aiService.completion(completionRequest);

// 工作流
Map<String, Object> inputs = new HashMap<>();
inputs.put("task_name", "完成项目文档");
inputs.put("priority", "high");
WorkflowRequest workflowRequest = new WorkflowRequest(inputs, "user123");
workflowRequest.setWorkflowId("task-management-workflow");
WorkflowResponse workflowResponse = aiService.workflow(workflowRequest);
```

### 2. 通过工厂模式使用

```java
@Autowired
private AiClientFactory clientFactory;

// 获取Dify客户端
AiClient difyClient = clientFactory.getClient("DIFY");
if (difyClient != null && difyClient.isAvailable()) {
    ChatResponse response = difyClient.chat(chatRequest);
}
```

### 3. 通过REST API使用

```bash
# 聊天
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好"}'

# 文本完成
curl -X POST http://localhost:8080/api/ai/completion \
  -H "Content-Type: application/json" \
  -d '{"prompt": "请写一首诗"}'

# 工作流
curl -X POST http://localhost:8080/api/ai/workflow \
  -H "Content-Type: application/json" \
  -d '{
    "inputs": {
      "task_name": "完成项目文档",
      "priority": "high"
    },
    "userId": "user123",
    "workflowId": "task-workflow"
  }'

# 简化工作流
curl -X POST "http://localhost:8080/api/ai/simple-workflow" \
  -d "inputs[task_name]=完成项目文档&inputs[priority]=high&userId=user123"
```

## 扩展指南

### 添加新的AI服务提供商

1. 实现 `AiClient` 接口：
```java
@Component
public class OpenAiClient implements AiClient {
    // 实现接口方法
}
```

2. 在 `AiClientFactory` 中注册：
```java
@PostConstruct
private void initializeClients() {
    clients.put("OPENAI", new OpenAiClient());
}
```

3. 添加配置支持

## 测试

运行测试：
```bash
mvn test -Dtest=AiServiceTest
```

测试覆盖：
- 聊天功能测试
- 文本完成功能测试
- 工作流功能测试
- 错误处理测试
- 服务可用性检查

## 注意事项

1. **当前实现状态**：使用模拟响应，生产环境需要配置真实的Dify API密钥和URL

2. **依赖版本**：dify-java-client 1.4.5

3. **配置要求**：确保正确配置 `dify.app-api-key` 和 `dify.base-url`

4. **错误处理**：所有API调用都有完整的异常处理和日志记录

5. **扩展性**：架构设计支持多种AI服务提供商的无缝切换

## 下一步计划

1. 集成真实的 dify-java-client API 调用
2. 添加流式响应支持
3. 实现文件上传功能
4. 添加更多AI服务提供商支持
5. 完善监控和指标收集

## 相关文档

- [dify-java-client GitHub](https://github.com/yuanbaobaoo/dify-java-client)
- [Dify 官方文档](https://docs.dify.ai/)
- [Spring Boot 文档](https://spring.io/projects/spring-boot)
