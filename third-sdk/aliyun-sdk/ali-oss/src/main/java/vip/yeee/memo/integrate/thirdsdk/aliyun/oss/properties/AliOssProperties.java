package vip.yeee.memo.integrate.thirdsdk.aliyun.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 16:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOssProperties {
    private String accessKeyId;
    private String accessKeySecret;
    /**
     * Region ID，默认：华东1（杭州）
     */
    private String region = "oss-cn-hangzhou";
    /**
     * Endpoint，默认：华东1（杭州）接入点
     */
    private String endpoint = "oss-cn-hangzhou-internal.aliyuncs.com";
    private String bucketDefault;
}
