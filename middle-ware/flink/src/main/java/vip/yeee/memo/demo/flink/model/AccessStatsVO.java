package vip.yeee.memo.demo.flink.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/6 18:45
 */
@Data
public class AccessStatsVO {

    private String processTime;

    private String tag;

    private Integer uv;

    private Integer pv;

    private List<Visitor> visitorList;

    private List<Subject> subjectList;

    @Data
    public static class Visitor {
        private String ip;
        private Integer count;
        private String city;
        private String device;
    }

    @Data
    public static class Subject {
        private Long id;
        private String name;
        private Integer count;
    }

}
