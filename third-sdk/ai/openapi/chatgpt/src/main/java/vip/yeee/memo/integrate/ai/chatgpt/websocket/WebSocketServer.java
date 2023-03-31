package vip.yeee.memo.integrate.ai.chatgpt.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;
import vip.yeee.memo.integrate.ai.chatgpt.service.ChatService;
import vip.yeee.memo.integrate.ai.chatgpt.util.WebSocketUtil;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/openai/ws/chat/{chatId}")
public class WebSocketServer {

    //在线总数
    private static int onlineCount;
    //当前会话
    private Session session;
    //用户id -目前是按浏览器随机生成
    private String uid;
    private String chatId;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 用来存放每个客户端对应的WebSocketServer对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap();

    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
    private final static List<Session> SESSIONS = Collections.synchronizedList(new ArrayList<>());


    /**
     * 建立连接
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") String chatId) {
        this.session = session;
        this.uid = WebSocketUtil.getIp(session);
        this.chatId = chatId;
        webSocketSet.add(this);
        SESSIONS.add(session);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            webSocketMap.put(uid, this);
        } else {
            webSocketMap.put(uid, this);
            addOnlineCount();
        }
        log.info("[UID：{}，CHATID：{}] 建立连接, 当前连接数:{}", this.uid, chatId, getOnlineCount());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            subOnlineCount();
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", uid, getOnlineCount());
    }

    /**
     * 发送错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.uid, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 接收到客户端消息
     * @param msg
     */
    @OnMessage
    public void onMessage(String msg) {
        log.info("[连接ID:{}] 收到消息:{}", this.uid, msg);
        ChatParams params = new ChatParams();
        ChatService chatService = (ChatService)SpringContextUtils.getBean(ChatService.class);
        List<ChatMessage> messages = chatService.getContext(chatId, 2);
        messages.add(chatService.buildUserMessage(msg));
        params.setMessages(messages);

        chatService.doWsChat(params, chatId, this.session);
    }


    /**
     * 获取当前连接数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 当前连接数加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前连接数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

