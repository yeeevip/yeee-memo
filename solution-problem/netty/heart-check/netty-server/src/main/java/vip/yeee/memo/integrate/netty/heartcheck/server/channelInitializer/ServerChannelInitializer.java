package vip.yeee.memo.integrate.netty.heartcheck.server.channelInitializer;


import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Message;

import java.util.concurrent.TimeUnit;

@Component(value = "serverChannelInitializer")
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${server.READER_IDLE_TIME_SECONDS}")
    private int READER_IDLE_TIME_SECONDS;

    @Value("${server.WRITER_IDLE_TIME_SECONDS}")
    private int WRITER_IDLE_TIME_SECONDS;

    @Value("${server.ALL_IDLE_TIME_SECONDS}")
    private int ALL_IDLE_TIME_SECONDS;

    @Autowired
    @Qualifier("serverHeartHandler")
    private ChannelInboundHandlerAdapter serverHeartHandler;

    @Autowired
    @Qualifier("otherServerHandler")
    private ChannelInboundHandlerAdapter otherServerHandler;
    
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
    	ChannelPipeline p = socketChannel.pipeline();
    	// 检测空闲必须放在这里 因为pipeline是分顺序加载的
    	p.addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS
    			, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS, TimeUnit.SECONDS));
    	//解码器必须放在前面，否则发数据收不到
        p.addLast(new ProtobufVarint32FrameDecoder()); // 添加protobuf解码器
        p.addLast(new ProtobufDecoder(Message.MessageBase.getDefaultInstance())); // 添加protobuf对应类解码器
        p.addLast(new ProtobufVarint32LengthFieldPrepender()); // protobuf的编码器 和上面对对应
        p.addLast(new ProtobufEncoder()); // protobuf的编码器

        //自定义的handler
        p.addLast("serverHeartHandler", serverHeartHandler);
        p.addLast("otherServerHandler", otherServerHandler);
    }
}
