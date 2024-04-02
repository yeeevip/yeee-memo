package vip.yeee.memo.demo.netty.simple.chat.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/5/12 16:36
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 所有连接的客户端Channel
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 当有客户端连接时，将其加入ChannelGroup中
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当有客户端断开连接时，从ChannelGroup中移除
        Channel channel = ctx.channel();
        channelGroup.remove(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        ctx.writeAndFlush(Unpooled.copiedBuffer(msg + "\n", CharsetUtil.UTF_8));
        Channel channel = ctx.channel();
        // 将消息广播给所有连接的客户端
        channelGroup.writeAndFlush(Unpooled.copiedBuffer(String.format("[%s]：" + msg + "\n", channel.id()), CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
