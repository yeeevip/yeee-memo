package vip.yeee.memo.integrate.design.practice.statics.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/29 10:01
 */
@Data
public class StaticAdVo {

    private String spaceCode;

    private String spaceName;

    private String supportType;

    private String launchType;

    private Integer whetherSetSkip;

    private Integer displayDuration;

    private List<AdItem> advertList;

    @Data
    public static class AdItem {

        private String id;

        private String title;

        private Integer type;

        private String advertUrl;

        private String launchId;

        private String videoUrl;

        private Integer sort;

        private List<Channel> channelList;

        private List<Pic> picList;

        private List<ValidTime> validTimeList;
    }

    @Data
    public static class Channel {

        private String channelId;

        private String channelName;

        private Integer channelStation;

        private Integer priority;

        private Integer sort;
    }

    @Data
    public static class Pic {

        private String id;

        private String ratio;

        private String url;
    }

    @Data
    public static class ValidTime {

        @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
        private LocalDateTime startTime;

        @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
        private LocalDateTime endTime;
    }
}
