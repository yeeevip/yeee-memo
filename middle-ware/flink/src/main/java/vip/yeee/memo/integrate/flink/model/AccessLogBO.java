package vip.yeee.memo.integrate.flink.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccessLogBO {

    /**
     * @see vip.yeee.memo.integrate.flink.constant.OperateEventEnum
     */
    private String event;
    private Long subjectId;
    private Date timestamp;
    private String ip;
    private String domain;
    private Integer status;
    private Float responseTime;
    private String device;

}
