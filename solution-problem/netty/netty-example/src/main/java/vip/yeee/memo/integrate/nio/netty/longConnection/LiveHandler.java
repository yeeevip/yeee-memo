package vip.yeee.memo.integrate.nio.netty.longConnection;

import vip.yeee.memo.integrate.nio.netty.longConnection.decoder.LiveMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 1.客户端发送的是msg且10s内没有发送心跳，则过期关闭连接并清除缓存
 * 2.客户端在每个时间窗口5s内没有发送heart，则主动关闭连接
 *
 * @author yeeee
 * @since 2021/12/14 18:23
 */
public class LiveHandler extends SimpleChannelInboundHandler<LiveMessage> {

    private static Map<Integer, LiveChannelCache> channelCache = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(LiveHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LiveMessage msg) throws Exception {
        Channel channel = ctx.channel();
        final int hashCode = channel.hashCode();
        logger.debug("channel hashCode: " + hashCode + " msg: " + msg + " cache: " + channelCache.size());

        if (!channelCache.containsKey(hashCode)) {
            logger.debug("channelCache.containsKey(hashCode), put key: " + hashCode);
            channel.closeFuture().addListener(future -> {
                logger.debug("channel close, remove key: " + hashCode);
                channelCache.remove(hashCode);
            });
            ScheduledFuture<?> scheduledFuture = ctx.executor().schedule(() -> {
                logger.debug("schedule runs, close channel: " + hashCode);
                channel.close();
            }, 10, TimeUnit.SECONDS);
            channelCache.put(hashCode, new LiveChannelCache(channel, scheduledFuture));
        }

        switch (msg.getType()) {
            case LiveMessage.TYPE_HEART: {
                logger.debug("switch type 1");
                LiveChannelCache cache = channelCache.get(hashCode);
                ScheduledFuture<ChannelFuture> scheduledFuture = ctx.executor().schedule((Callable<ChannelFuture>) channel::close, 5, TimeUnit.SECONDS);
                cache.getScheduledFuture().cancel(true); // 取消
                cache.setScheduledFuture(scheduledFuture); //  新的5s
                ctx.channel().writeAndFlush(msg.getContent());
                break;
            }
            case LiveMessage.TYPE_MESSAGE: {
                logger.debug("switch type 2");
                channelCache.forEach((key, value) -> {
                    Channel otherChannel = value.getChannel();
                    logger.debug("write msg to otherChannel：" + otherChannel + " msg：" + msg);
                    otherChannel.writeAndFlush(msg.getContent());
                });
                break;
            }
            default:
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.debug("exceptionCaught");
        if (cause != null) cause.printStackTrace();
        if (ctx != null) ctx.close();
    }
}
