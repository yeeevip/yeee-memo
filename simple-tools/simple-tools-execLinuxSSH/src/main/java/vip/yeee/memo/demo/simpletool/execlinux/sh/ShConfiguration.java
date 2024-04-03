package vip.yeee.memo.demo.simpletool.execlinux.sh;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/27 11:38
 */
@Configuration
@EnableConfigurationProperties(ShConfiguration.Properties.class)
public class ShConfiguration {

    @ConfigurationProperties("yeee.exec.sh")
    @Data
    public static class Properties {
        private Timeouts timeouts = new Timeouts();
    }

    @Data
    public static class Timeouts {
        private Duration connect = Duration.ofSeconds(5);
        private Duration auth = Duration.ofSeconds(5);
        private Duration openChannel = Duration.ofSeconds(5);
        private Duration execution = Duration.ofHours(1);
    }

}
