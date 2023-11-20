package vip.yeee.memo.base.web.handle;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志打印进行相应脱敏处理
 */
@Slf4j
public class LogSensitiveMessageConverter extends MessageConverter {

    /**
     * 排除日志
     */
    private final Set<String> excludeLevel = new HashSet<>();

    private final Set<String> excludeName = new HashSet<>();

    public Set<String> addExcludeLevel(String excludeLevel) {
        this.excludeLevel.add(excludeLevel);
        return this.excludeLevel;
    }

    public Set<String> addExcludeName(String excludeName) {
        this.excludeName.add(excludeName);
        return this.excludeName;
    }

    /**
     * 正则规则
     */
    private static final String[] PATTERNS = new String[]{
            // 身份证 15、18位数字
            "\\d{17}[\\d|x]|\\d{15}",
            // 邮箱
            "\\w+?@\\w+?.com",
            // 手机号
            "1[345789]\\d{9}",
    };

    @Override
    public String convert(ILoggingEvent event) {
        if (!excludeLevel.contains(event.getLevel().levelStr) && !excludeName.contains(event.getLoggerName())) {
            return sensitiveMessage(event.getFormattedMessage());
        }
        return event.getFormattedMessage();
    }

    /**
     * 敏感信息
     */
    private String sensitiveMessage(String message) {
        for (String pattern : PATTERNS) {
            message = replaceMessage(message, pattern);
        }
        return message;
    }

    /**
     * 替换
     */
    private String replaceMessage(String message, String pattern) {
        Set<String> matchSet = matchPattern(message, pattern);
        if (CollectionUtil.isNotEmpty(matchSet)) {
            for (String match : matchSet) {
                message = message.replaceAll(match, match.substring(0, 3) + "****" + match.substring(match.length() - 4));
            }
        }
        return message;
    }

    /**
     * 匹配正则
     */
    private Set<String> matchPattern(String message, String pattern) {
        Set<String> matchSet = new HashSet<>();
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(message);
        while (matcher.find()) {
            matchSet.add(matcher.group());
        }
        return matchSet;
    }

}