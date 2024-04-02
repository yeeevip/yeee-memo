package vip.yeee.memo.common.encrypt.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/10/24 10:25
 */
@Data
@Component
@ConfigurationProperties(prefix = "yeee.mybatis.encrypt")
public class MybatisEncryptProperty {

    private String password;
}
