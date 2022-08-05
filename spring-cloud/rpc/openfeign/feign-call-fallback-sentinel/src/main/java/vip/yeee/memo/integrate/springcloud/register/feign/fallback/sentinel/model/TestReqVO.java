package vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.model;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/5 14:49
 */
@Data
public class TestReqVO {

    // POST-JSON请求的字符串格式为时间
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private LocalDateTime startDate;

    // GET请求url中参数或者POST formData参数将字符串转为时间
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endDate;

}
