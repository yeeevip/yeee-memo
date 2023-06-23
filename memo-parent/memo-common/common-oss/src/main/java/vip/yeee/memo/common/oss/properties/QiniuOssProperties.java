package vip.yeee.memo.common.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/6/12 16:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "yeee.oss.qiniu")
public class QiniuOssProperties {

    private String accessKey;

    private String secretKey;

    private String defaultBucket;

    private String domain;

}
