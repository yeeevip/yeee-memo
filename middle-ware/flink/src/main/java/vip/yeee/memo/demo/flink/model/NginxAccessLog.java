package vip.yeee.memo.demo.flink.model;

import lombok.Data;

@Data
public class NginxAccessLog {

    private String timestamp;
    private String clientIp;
    private String requestMethod;
    private String domain;
    private String referer;
    private String requestUrl;
    private String requestArgs;
    private String status;
    private String responseTime;
    private String userAgent;
    private String httpCookie;

}
