package vip.yeee.memo.integrate.nio.netty.httpserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/14 16:48
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> { // Handler需要声明泛型为<FullHttpRequest>，声明之后，只有msg为FullHttpRequest的消息才能进来。

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        System.out.println("class: " + msg.getClass().getName());
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("test".getBytes(StandardCharsets.UTF_8)));

        HttpHeaders headers = response.headers();
        headers.add(HttpHeaderNames.CONTENT_TYPE, contentType + ";charset=utf-8");
        // 添加header描述length。这一步是很重要的一步，如果没有这一步，你会发现用postman发出请求之后就一直在刷新，因为http请求方不知道返回的数据到底有多长
        headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        // channel读取完成之后需要输出缓冲流。如果没有这一步，你会发现postman同样会一直在刷新
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        if (cause != null) cause.printStackTrace();
        if (ctx != null) ctx.close();
    }
}
