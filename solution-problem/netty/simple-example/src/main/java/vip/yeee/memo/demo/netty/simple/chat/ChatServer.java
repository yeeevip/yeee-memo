package vip.yeee.memo.demo.netty.simple.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import vip.yeee.memo.demo.netty.simple.chat.handler.ChatServerHandler;

import java.util.List;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/5/12 16:34
 */
public class ChatServer {

    private static final int PORT = 8083;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    // 解码器: 将字节数组转换为字符串
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                                    new MessageToMessageDecoder<ByteBuf>() {
                                        @Override
                                        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                            // 读取字符串
                                            out.add(in.toString(CharsetUtil.UTF_8));
                                        }
                                    },
                                    // 编码器: 在消息前面添加4个字节的长度
                                    new LengthFieldPrepender(4),
                                    // 自定义业务处理器: 处理接收到的消息
                                    new ChatServerHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定并开始接受传入连接请求
            ChannelFuture f = b.bind(PORT).sync();

            // 等待服务器套接字关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
