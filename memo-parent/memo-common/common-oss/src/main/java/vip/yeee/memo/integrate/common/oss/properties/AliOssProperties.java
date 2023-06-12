package vip.yeee.memo.integrate.common.oss.properties;

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
@ConfigurationProperties(prefix = "yeee.oss.ali")
public class AliOssProperties {

    private String accessKey;

    private String secretKey;
    /**
     * Region ID，默认：华东1（杭州）
     */
    private String region = "oss-cn-hangzhou";
    /**
     * Endpoint，默认：华东1（杭州）接入点
     */
    private String endpoint = "oss-cn-hangzhou-internal.aliyuncs.com";

    private String defaultBucket;

    private Sts sts;

    @Data
    public static class Sts {

        private String roleArn;

        private String roleSessionName;

    }

}
