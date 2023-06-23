package vip.yeee.memo.demo.thirdsdk.aliyun.nls.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "yeee.aliyun.nls")
public class AliyunNlsProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String appKey;

}