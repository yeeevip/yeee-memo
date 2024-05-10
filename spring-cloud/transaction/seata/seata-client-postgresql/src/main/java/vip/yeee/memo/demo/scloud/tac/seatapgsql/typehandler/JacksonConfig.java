package vip.yeee.memo.demo.scloud.tac.seatapgsql.typehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/5/10 10:18
 */
@Configuration
public class JacksonConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new GeometryModule());
    }
}
