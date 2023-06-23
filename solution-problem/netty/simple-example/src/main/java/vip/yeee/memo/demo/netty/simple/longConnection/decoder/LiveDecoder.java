package vip.yeee.memo.demo.netty.simple.longConnection.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.demo.netty.simple.longConnection.model.LiveMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author yeeee
 * @since 2021/12/14 18:07
 */
@Slf4j
public class LiveDecoder extends ReplayingDecoder<LiveDecoder.LiveState> { // 继承ReplayingDecoder，泛型LiveState，用来表示当前读取的状态

    public enum LiveState { // 描述LiveState，有读取长度和读取内容两个状态
        TYPE,
        LENGTH,
        CONTENT
    }

    private final LiveMessage message = new LiveMessage();

    public LiveDecoder() {
        super(LiveState.TYPE); // 初始化的时候设置为读取类型的状态
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) { // 读取的时候通过state()方法来确定当前处于什么状态
            case TYPE:
                byte type = in.readByte();
                log.debug("decode msg，state = TYPE，type = " + type);
                message.setType(type);
                checkpoint(LiveState.LENGTH);
                break;
            case LENGTH:
                int length = in.readInt();
                log.debug("decode msg，state = LENGTH，length = " + length);
                message.setLength(length);
                if (length > 0) {
                    checkpoint(LiveState.CONTENT); // 如果读取出来的长度大于0，则设置为读取内容状态，下一次读取的时候则从这个位置开始
                } else {
                    out.add(message);
                    checkpoint(LiveState.TYPE);
                }
                break;
            case CONTENT:
                byte[] bytes = new byte[message.getLength()];
                in.readBytes(bytes);
                message.setContent(new String(bytes, StandardCharsets.UTF_8));
                out.add(message);
                log.debug("decode msg，state = CONTENT，content = " + message);
                checkpoint(LiveState.TYPE);
                break;
            default:
                throw new IllegalMonitorStateException("invalid state: " + state());
        }
    }
}
