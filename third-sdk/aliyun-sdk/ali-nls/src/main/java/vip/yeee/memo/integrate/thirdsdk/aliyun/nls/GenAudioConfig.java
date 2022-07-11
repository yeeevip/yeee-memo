package vip.yeee.memo.integrate.thirdsdk.aliyun.nls;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.nls.gen-audio")
public class GenAudioConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String appKey;
    private Integer concurrentNum;
    private Integer isWav;
    private Integer splitSize;
    private Integer longMode;
    private String voice;
}
