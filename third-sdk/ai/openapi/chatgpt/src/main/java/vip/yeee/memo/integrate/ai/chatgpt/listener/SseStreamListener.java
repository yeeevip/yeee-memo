package vip.yeee.memo.integrate.ai.chatgpt.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;

/**
 * sse
 *
 * @author plexpt
 */
@Slf4j
@RequiredArgsConstructor
public class SseStreamListener extends AbstractStreamListener {

    final SseEmitter sseEmitter;
    final String msgId = IdUtil.simpleUUID();

    @Override
    public void onMsg(String message) {
        try {
            log.info("收到消息：{}", message);
            Message msg = new Message();
            msg.setMsgId(msgId);
            msg.setKind("chat");
            msg.setMsg(message);
            msg.setCreateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
            sseEmitter.send(JSON.toJSONString(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable, String response) {
//        sseEmitter.complete();
    }

    @Data
    private static class Message {
        private String kind;
        private String msg;
        private String msgId;
        private String createTime;
    }

}
