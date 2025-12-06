package org.syj.geotask.dify.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.syj.geotask.dify.client.impl.DifyAiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI客户端工厂
 * 负责管理和创建不同类型的AI客户端实例
 */
@Slf4j
@Component
public class AiClientFactory {
    
    private final Map<String, AiClient> clients = new HashMap<>();
    
    @Autowired
    public AiClientFactory(List<AiClient> aiClients) {
        // 自动注入所有实现了AiClient接口的Bean
        for (AiClient client : aiClients) {
            registerClient(client);
        }
    }
    
    /**
     * 注册AI客户端
     * @param client AI客户端实例
     */
    public void registerClient(AiClient client) {
        String clientType = client.getClientType();
        clients.put(clientType, client);
        log.info("注册AI客户端: {}, 可用性: {}", clientType, client.isAvailable());
    }
    
    /**
     * 获取指定类型的AI客户端
     * @param clientType 客户端类型
     * @return AI客户端实例
     * @throws IllegalArgumentException 当客户端类型不存在时抛出
     */
    public AiClient getClient(String clientType) {
        AiClient client = clients.get(clientType);
        if (client == null) {
            throw new IllegalArgumentException("不支持的AI客户端类型: " + clientType);
        }
        return client;
    }
    
    /**
     * 获取默认的AI客户端
     * @return 默认AI客户端实例
     */
    public AiClient getDefaultClient() {
        // 优先返回可用的Dify客户端
        AiClient difyClient = clients.get("DIFY");
        if (difyClient != null && difyClient.isAvailable()) {
            return difyClient;
        }
        
        // 如果Dify不可用，返回第一个可用的客户端
        for (AiClient client : clients.values()) {
            if (client.isAvailable()) {
                return client;
            }
        }
        
        throw new IllegalStateException("没有可用的AI客户端");
    }
    
    /**
     * 获取所有可用的AI客户端
     * @return 可用的AI客户端列表
     */
    public Map<String, AiClient> getAvailableClients() {
        Map<String, AiClient> availableClients = new HashMap<>();
        for (Map.Entry<String, AiClient> entry : clients.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableClients.put(entry.getKey(), entry.getValue());
            }
        }
        return availableClients;
    }
    
    /**
     * 获取所有注册的客户端类型
     * @return 客户端类型列表
     */
    public java.util.Set<String> getRegisteredClientTypes() {
        return clients.keySet();
    }
    
    /**
     * 检查指定类型的客户端是否可用
     * @param clientType 客户端类型
     * @return 是否可用
     */
    public boolean isClientAvailable(String clientType) {
        AiClient client = clients.get(clientType);
        return client != null && client.isAvailable();
    }
}
