package vip.yeee.memo.integrate.flink.process;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import vip.yeee.memo.integrate.flink.constant.OperateEventEnum;
import vip.yeee.memo.integrate.flink.model.AccessLogBO;
import vip.yeee.memo.integrate.flink.model.AccessStatsVO;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/6 18:44
 */
@Slf4j
public class AccessEventProcessWindowFunction extends ProcessWindowFunction<AccessLogBO, AccessStatsVO, String, TimeWindow> {

    @Override
    public void process(String s, ProcessWindowFunction<AccessLogBO, AccessStatsVO, String, TimeWindow>.Context context
            , Iterable<AccessLogBO> elements, Collector<AccessStatsVO> out) throws Exception {
        log.info("开始计算 - KEY = {}", s);
        HashMap<String, AccessStatsVO.Visitor> visitorMap = Maps.newHashMap();
        HashMap<Long, AccessStatsVO.Subject> subjectMap = Maps.newHashMap();
        AccessStatsVO accessStatsVO = new AccessStatsVO();
        accessStatsVO.setProcessTime(DateUtil.format(new Date(context.currentProcessingTime()), DatePattern.UTC_SIMPLE_PATTERN));
        accessStatsVO.setTag(s);
        int uv = 0, pv = 0;
        for (AccessLogBO bo : elements) {
            AccessStatsVO.Visitor existsVisitor = visitorMap.get(bo.getIp());
            if (existsVisitor != null) {
                existsVisitor.setCount(existsVisitor.getCount() + 1);
                existsVisitor.setDevice(bo.getDevice());
                visitorMap.put(bo.getIp(), existsVisitor);
            } else {
                AccessStatsVO.Visitor visitor = new AccessStatsVO.Visitor();
                visitor.setIp(bo.getIp());
                visitor.setCount(1);
                visitor.setDevice(bo.getDevice());
                visitorMap.put(bo.getIp(), visitor);
                uv++;
            }
            if (s.startsWith(OperateEventEnum.VIEW_DETAIL.getEvnet())) {
                AccessStatsVO.Subject existsSubject = subjectMap.get(bo.getSubjectId());
                if (existsSubject != null) {
                    existsSubject.setCount(existsSubject.getCount() + 1);
                    visitorMap.put(bo.getIp(), existsVisitor);
                } else {
                    AccessStatsVO.Subject subject = new AccessStatsVO.Subject();
                    subject.setId(bo.getSubjectId());
                    subject.setCount(1);
                    subjectMap.put(bo.getSubjectId(), subject);
                }
            }
            pv++;
        }
        List<AccessStatsVO.Visitor> visitorList = visitorMap.values()
                .stream()
                .sorted(Comparator.comparing(AccessStatsVO.Visitor::getCount))
                .limit(10)
                .collect(Collectors.toList());
        List<AccessStatsVO.Subject> subjectList = subjectMap.values()
                .stream()
                .sorted(Comparator.comparing(AccessStatsVO.Subject::getCount))
                .limit(10)
                .collect(Collectors.toList());
        accessStatsVO.setUv(uv);
        accessStatsVO.setPv(pv);
        accessStatsVO.setVisitorList(visitorList);
        accessStatsVO.setSubjectList(subjectList);
        out.collect(accessStatsVO);
    }

}
