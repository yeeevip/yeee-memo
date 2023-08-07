package vip.yeee.memo.common.websocket.netty.support.argument;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import vip.yeee.memo.common.websocket.netty.annotation.PathParam;

import java.util.Collections;
import java.util.Map;

import static vip.yeee.memo.common.websocket.netty.bootstrap.WsEndpointDispatcher.URI_TEMPLATE;

public class PathParamMapMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        PathParam ann = parameter.getParameterAnnotation(PathParam.class);
        return (ann != null && Map.class.isAssignableFrom(parameter.getParameterType()) &&
                !StringUtils.hasText(ann.value()));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        PathParam ann = parameter.getParameterAnnotation(PathParam.class);
        String name = ann.name();
        if (name.isEmpty()) {
            name = parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument type [" + parameter.getNestedParameterType().getName() +
                                "] not available, and parameter name information not found in class file either.");
            }
        }
        Map<String, String> uriTemplateVars = channel.attr(URI_TEMPLATE).get();
        if (!CollectionUtils.isEmpty(uriTemplateVars)) {
            return uriTemplateVars;
        } else {
            return Collections.emptyMap();
        }
    }
}
