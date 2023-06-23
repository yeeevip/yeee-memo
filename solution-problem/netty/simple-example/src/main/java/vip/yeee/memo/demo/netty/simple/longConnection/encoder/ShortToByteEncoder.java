package vip.yeee.memo.demo.netty.simple.longConnection.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author yeeee
 * @since 2021/12/14 18:03
 */
@Slf4j
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        log.debug("ShortToByteEncoder encode msg = " + msg);
        out.writeShort(msg);
    }
}

