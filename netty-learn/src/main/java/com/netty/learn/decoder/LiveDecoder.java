package com.netty.learn.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/12/14 18:07
 */
public class LiveDecoder extends ReplayingDecoder<LiveDecoder.LiveState> { // 继承ReplayingDecoder，泛型LiveState，用来表示当前读取的状态

    public enum LiveState { // 描述LiveState，有读取长度和读取内容两个状态
        LENGTH,
        CONTENT
    }

    private LiveMessage message = new LiveMessage();

    public LiveDecoder() {
        super(LiveState.LENGTH); // 初始化的时候设置为读取长度的状态
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) { // 读取的时候通过state()方法来确定当前处于什么状态
            case LENGTH:
                int length = in.readInt();
                if (length > 0) {
                    message.setLength(length);
                    checkpoint(LiveState.CONTENT); // 如果读取出来的长度大于0，则设置为读取内容状态，下一次读取的时候则从这个位置开始
                } else {
                    out.add(message);
                }
                break;
            case CONTENT:
                byte[] bytes = new byte[message.getLength()];
                in.readBytes(bytes);
                message.setContent(new String(bytes, StandardCharsets.UTF_8));
                out.add(message);
                break;
            default:
                throw new IllegalMonitorStateException("invalid state: " + state());
        }
    }
}

class LiveMessage {

    private Integer length;

    private String content;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
