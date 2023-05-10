package vip.yeee.memo.integrate.netty.simple.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import vip.yeee.memo.integrate.netty.simple.chat.handler.ChatClientHandler;

import java.util.List;
import java.util.Scanner;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/5/12 16:39
 */
public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8083;

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    // 编码器: 在消息前面添加4个字节的长度
                                    new LengthFieldPrepender(4),
                                    // 解码器: 将字节数组转换为字符串
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                                    new MessageToMessageDecoder<ByteBuf>() {
                                        @Override
                                        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                            // 读取字符串
                                            out.add(in.toString(CharsetUtil.UTF_8));
                                        }
                                    },
                                    // 自定义业务处理器: 处理接收到的消息
                                    new ChatClientHandler()
                            );
                        }
                    });
            // 连接到服务器
            ChannelFuture f = b.connect(HOST, PORT).sync();
            // 获取控制台输入
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                // 发送消息到服务器
                f.channel().writeAndFlush(Unpooled.wrappedBuffer(input.getBytes(CharsetUtil.UTF_8)));
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
