package vip.yeee.memo.integrate.flink.convert;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.integrate.base.util.JacksonUtils;
import vip.yeee.memo.integrate.flink.constant.OperateEventEnum;
import vip.yeee.memo.integrate.flink.model.AccessLogBO;
import vip.yeee.memo.integrate.flink.model.NginxAccessLog;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/2 14:21
 */
@Slf4j
public class LogDataConvert {

    public static AccessLogBO nginxLog2AccessBO(String logStr) {
        try {
            NginxAccessLog nginxAccessLog = JacksonUtils.toJavaBean(logStr, NginxAccessLog.class);
            OperateEventSubject eventSubject = LogDataConvert.getEventSubject(nginxAccessLog);
            if (eventSubject == null) {
                return null;
            }
            AccessLogBO bo = new AccessLogBO();
            bo.setEvent(eventSubject.getEvent());
            bo.setSubjectId(eventSubject.getSubjectId());
            bo.setTimestamp(DateUtil.parse(nginxAccessLog.getTimestamp()));
            bo.setIp(StrUtil.removePrefix(nginxAccessLog.getClientIp(), "-"));
            bo.setDomain(StrUtil.removePrefix(nginxAccessLog.getDomain(), "-"));
            bo.setStatus(Integer.parseInt(nginxAccessLog.getStatus()));
            bo.setResponseTime(Float.parseFloat(nginxAccessLog.getResponseTime()));
            bo.setDevice(UserAgentUtil.parse(nginxAccessLog.getUserAgent()).getOs().getName());
            return bo;
        } catch (Exception e) {
            log.error("nginxLog2AccessBO error，logStr = {}", logStr, e);
        }
        return null;
    }

    private static OperateEventSubject getEventSubject(NginxAccessLog nginxAccessLog) {
        String domain = nginxAccessLog.getDomain();
        String requestUrl = nginxAccessLog.getRequestUrl();
        if (!StrUtil.containsAny(domain, ".com", ".vip")) {
            return null;
        }
        if ("/".equals(requestUrl)) {
            return new OperateEventSubject(OperateEventEnum.VIEW_INDEX.getEvnet(), null);
        }
        if (StrUtil.contains(requestUrl, "/getList")) {
            Map<String, Object> reqParams = LogDataConvert.getReqParams(nginxAccessLog.getRequestArgs());
            String pageNum = (String) reqParams.get("pageNum");
            return new OperateEventSubject(OperateEventEnum.VIEW_LIST.getEvnet(), StrUtil.isNotBlank(pageNum) ? Long.parseLong(pageNum) : null);
        }
        if (StrUtil.contains(requestUrl, "/detail.html")) {
            Map<String, Object> reqParams = LogDataConvert.getReqParams(nginxAccessLog.getRequestArgs());
            String id = (String) reqParams.get("id");
            return new OperateEventSubject(OperateEventEnum.VIEW_DETAIL.getEvnet(), StrUtil.isNotBlank(id) ? Long.parseLong(id) : null);
        }
        return null;
    }

    private static Map<String, Object> getReqParams(String urlArgs) {
        if (StrUtil.isBlank(urlArgs)) {
            return Collections.emptyMap();
        }
        String decodeUrl = URLUtil.decode(urlArgs);
        HashMap<String, Object> map = Maps.newHashMap();
        Stream.of(decodeUrl.split("&"))
                .filter(StrUtil::isNotBlank)
                .forEach(param -> {
                    String[] split = param.split("=");
                    if (split.length > 1) {
                        map.put(split[0], split[1]);
                    }
                });
        return map;
    }

    @Data
    @AllArgsConstructor
    static class OperateEventSubject {
        private String event;
        private Long subjectId;
    }

}
