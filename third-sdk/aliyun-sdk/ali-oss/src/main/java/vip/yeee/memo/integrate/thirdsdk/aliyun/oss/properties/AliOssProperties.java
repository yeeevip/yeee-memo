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
    private String endpoint;
    private String bucketDefault;
}
