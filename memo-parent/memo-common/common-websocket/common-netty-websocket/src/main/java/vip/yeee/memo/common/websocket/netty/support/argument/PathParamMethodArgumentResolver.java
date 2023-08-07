package vip.yeee.memo.common.websocket.netty.support.argument;

import io.netty.channel.Channel;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;
import vip.yeee.memo.common.websocket.netty.annotation.PathParam;

import java.util.Map;

import static vip.yeee.memo.common.websocket.netty.bootstrap.WsEndpointDispatcher.URI_TEMPLATE;

public class PathParamMethodArgumentResolver implements MethodArgumentResolver {

    private final AbstractBeanFactory beanFactory;

    public PathParamMethodArgumentResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathParam.class);
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
        Object arg = (uriTemplateVars != null ? uriTemplateVars.get(name) : null);
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        return typeConverter.convertIfNecessary(arg, parameter.getParameterType());
    }
}
