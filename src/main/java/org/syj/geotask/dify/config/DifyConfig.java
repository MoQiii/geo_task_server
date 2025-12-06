package org.syj.geotask.dify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Dify配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dify")
public class DifyConfig {
    
    /**
     * Dify API基础URL
     */
    private String baseUrl = "http://localhost/v1";
    
    /**
     * 应用API密钥
     */
    private String appApiKey = "app-64jqmIYqUWG8gjqkV0iYj90q";
    
    /**
     * 数据集API密钥
     */
    private String datasetApiKey;
    
    /**
     * 默认用户ID
     */
    private String defaultUserId = "default-user";
    
    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 30000;
    
    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 60000;
    
    /**
     * 是否启用重试
     */
    private boolean retryEnabled = true;
    
    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
    
    /**
     * 重试间隔（毫秒）
     */
    private long retryInterval = 1000;
}
