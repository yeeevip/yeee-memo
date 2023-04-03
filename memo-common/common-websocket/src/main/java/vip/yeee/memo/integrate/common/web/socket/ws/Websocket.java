package vip.yeee.memo.integrate.common.web.socket.ws;

import javax.websocket.Session;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/8 16:54
 */
public class Websocket {

    /**
     * Use for: 规范连接回调
     * Explain:
     */
    interface LinkPoint {
        //连接时回调
        void onOpen(Session session, String auth);

        //收到消息时回调
        void onMessage(String message);

        //连接关闭时回调
        void onClose();

        //发生错误时回调
        void onError(Session session, Throwable throwable);
    }

    /**
     * Use for: 客户端
     * Explain:
     */
    interface Client {
        //获取会话
        Session getSession();

        //获取标记
        String getTag();

        //发送文本
        void sendText(String text);

        //发送对象
        void send(Object object);
    }

    /**
     * Use for: 管理行为
     * Explain:
     */
    interface Manager<T extends Client> {
        //向指定客户端发送文本
        void sendText(String text, T... clients);

        //向所有客户端发送文本
        void sendTextYoAll(String text);

        //向指定客户端发送对象
        void send(Object object, T... clients);

        //向所有客户端发送对象
        void sendToAll(Object object);

        //向其他客户端发送文本
        void sendTextToOther(String text, T... clients);

        //向其他客户端发送对象
        void sendToOther(Object object, T... clients);

        //添加客户端
        void addClients(T... clients);

        //获取所有客户端
        CopyOnWriteArraySet<T> all();

        //移除客户端
        void removeClients(T... clients);

        //根据标记获取客户端
        T getClientByTag(String tag);

        //根据标记获取多个客户端
        T[] getClientsByTags(String... tags);
    }
}