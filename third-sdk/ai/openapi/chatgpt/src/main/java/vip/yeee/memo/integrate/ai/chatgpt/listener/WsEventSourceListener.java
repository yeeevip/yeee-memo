package vip.yeee.memo.integrate.ai.chatgpt.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class WsEventSourceListener extends AbstractStreamListener {

    private final Session session;

    final String msgId = IdUtil.simpleUUID();

    public WsEventSourceListener(Session session) {
        this.session = session;
    }

    @Override
    public void onMsg(String message) {
        try {
            log.info("收到消息：{}", message);
            Message msg = new Message();
            msg.setMsgId(msgId);
            msg.setKind("chat");
            msg.setMsg(message);
            msg.setCreateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
            session.getBasicRemote().sendText(JSON.toJSONString(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable, String response) {
        Message msg = new Message();
        msg.setMsgId(msgId);
        msg.setKind("chat");
        msg.setMsg("系统开小差了，请稍后再试！！！");
        msg.setCreateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    private static class Message {
        private String kind;
        private String msg;
        private String msgId;
        private String createTime;
    }
}
