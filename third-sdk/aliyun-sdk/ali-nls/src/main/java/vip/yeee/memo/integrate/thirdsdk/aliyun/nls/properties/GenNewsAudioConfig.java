package vip.yeee.memo.integrate.thirdsdk.aliyun.nls.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "yeee.biz.gen-audio")
public class GenNewsAudioConfig {
    private Integer isOpen;
    private String startDate;
    private String endDate;
    private Integer batchNum;
    private Integer isWav;
    private Integer splitSize;
    private Integer concurrentNum = 2;
    private Integer longMode;
    private String voice;
}
