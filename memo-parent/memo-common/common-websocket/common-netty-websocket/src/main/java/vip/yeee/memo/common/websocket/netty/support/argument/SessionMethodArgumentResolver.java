package vip.yeee.memo.common.websocket.netty.support.argument;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import vip.yeee.memo.common.websocket.netty.bootstrap.Session;

import static vip.yeee.memo.common.websocket.netty.bootstrap.WsEndpointDispatcher.SESSION_KEY;

public class SessionMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Session.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        Session session = channel.attr(SESSION_KEY).get();
        return session;
    }
}
