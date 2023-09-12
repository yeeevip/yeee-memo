package vip.yeee.memo.base.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/9/12 10:47
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yeee.custom.redis")
public class CustomRedisProperty {

    private Boolean enabled = false;
    private List<CustomRedisProperties> instance;

    public static class CustomRedisProperties {
        private String beanName;
        private String host;
        private int port;
        private String password;
        private int database;
        private int timeout;

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDatabase() {
            return database;
        }

        public void setDatabase(int database) {
            this.database = database;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

    }
}
