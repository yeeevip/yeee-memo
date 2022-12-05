package vip.yeee.memo.integrate.stools.kit.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "yeee.okhttp")
public class OkhttpProperties {

    /**
     * 连接超时时间：30秒
     */
    private Integer connectTimeout = 30;
    /**
     * 读取超时时间：30秒
     */
    private Integer readTimeout = 30;
    /**
     * 写入超时时间：30秒
     */
    private Integer writeTimeout = 30;
    /**
     * 最大连接空闲时间：300秒
     */
    private Long keepAliveDuration = 300L;
    /**
     * 最大空闲连接数：200
     */
    private Integer maxIdleConnections = 200;

}
