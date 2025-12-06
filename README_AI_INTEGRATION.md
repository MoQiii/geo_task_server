# GeoTask AI集成说明

本项目集成了dify-java-client，为任务管理系统提供了智能AI功能。

## 功能特性

### 1. AI客户端抽象层
- `AiClient` 接口：定义了AI服务的基本操作
- `DifyAiClient` 实现：基于dify-java-client的具体实现
- 支持聊天对话和文本完成功能

### 2. 智能任务管理
- **任务建议**：为任务提供智能执行建议
- **优先级分析**：AI分析任务的重要性和紧急程度
- **智能对话**：与AI助手进行任务相关对话

### 3. REST API接口

#### AI功能接口
- `POST /api/ai/chat` - 聊天对话
- `POST /api/ai/completion` - 文本完成
- `POST /api/ai/task-suggestion` - 获取任务建议
- `POST /api/ai/task-priority-analysis` - 任务优先级分析
- `GET /api/ai/status` - 检查AI服务状态
- `POST /api/ai/simple-chat` - 简化聊天接口

#### 任务管理增强接口
- `POST /api/tasks/{id}/ai-suggestion` - 获取特定任务的AI建议
- `POST /api/tasks/{id}/priority-analysis` - 分析特定任务优先级
- `POST /api/tasks/ai-suggestion` - 创建任务时获取AI建议

## 配置说明

在 `application.properties` 中配置Dify相关参数：

```properties
# Dify Configuration
dify.base-url=https://api.dify.ai
dify.app-api-key=your-app-api-key
dify.dataset-api-key=your-dataset-api-key
dify.default-user-id=default-user
dify.connect-timeout=30000
dify.read-timeout=60000
dify.retry-enabled=true
dify.max-retries=3
dify.retry-interval=1000
```

## 使用示例

### 1. 获取任务建议
```bash
curl -X POST "http://localhost:8080/api/ai/task-suggestion" \
  -d "taskDescription=完成项目报告编写" \
  -d "userId=user123"
```

### 2. 分析任务优先级
```bash
curl -X POST "http://localhost:8080/api/ai/task-priority-analysis" \
  -d "taskTitle=紧急bug修复" \
  -d "taskDescription=客户反馈严重问题需要立即处理" \
  -d "userId=user123"
```

### 3. 简单聊天
```bash
curl -X POST "http://localhost:8080/api/ai/simple-chat" \
  -d "message=帮我分析一下这个任务的优先级" \
  -d "userId=user123"
```

### 4. 为现有任务获取建议
```bash
curl -X POST "http://localhost:8080/api/tasks/1/ai-suggestion" \
  -d "userId=user123"
```

## 架构设计

### 分层架构
```
Controller Layer (控制器层)
    ↓
Service Layer (服务层)
    ↓
Client Layer (客户端抽象层)
    ↓
Dify Java Client (具体实现)
```

### 核心组件

1. **AiClient接口**：定义AI操作的标准接口
2. **DifyAiClient**：Dify客户端的具体实现
3. **AiService**：提供高级AI服务功能
4. **DifyConfig**：配置管理
5. **模型类**：请求和响应的数据模型

## 扩展性

### 添加新的AI提供商
1. 实现 `AiClient` 接口
2. 创建对应的配置类
3. 在 `AiServiceImpl` 中添加选择逻辑
4. 更新配置文件

### 自定义AI功能
1. 在 `AiService` 接口中添加新方法
2. 在 `AiServiceImpl` 中实现具体逻辑
3. 在 `AiController` 中添加对应的REST接口

## 测试

运行测试类 `AiServiceTest` 来验证AI功能：

```bash
mvn test -Dtest=AiServiceTest
```

## 注意事项

1. **API密钥安全**：确保API密钥的安全存储
2. **错误处理**：所有AI调用都有完善的错误处理机制
3. **超时设置**：合理设置连接和读取超时时间
4. **重试机制**：支持自动重试失败的请求
5. **日志记录**：详细的日志记录便于调试和监控

## 未来规划

1. **流式响应**：支持流式AI响应
2. **多模态**：支持图片、语音等多模态输入
3. **缓存机制**：添加响应缓存提高性能
4. **监控告警**：添加AI服务的监控和告警
5. **A/B测试**：支持不同AI模型的A/B测试
