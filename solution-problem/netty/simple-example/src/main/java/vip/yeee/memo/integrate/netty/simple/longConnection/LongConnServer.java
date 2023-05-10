package vip.yeee.memo.integrate.netty.simple.longConnection;

import vip.yeee.memo.integrate.netty.simple.longConnection.decoder.LiveDecoder;
import vip.yeee.memo.integrate.netty.simple.longConnection.encoder.StringToByteEncoder;
import vip.yeee.memo.integrate.netty.simple.longConnection.handler.LiveHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author yeeee
 * @since 2021/12/15 11:40
 */
public class LongConnServer {

    public static void main(String[] args) throws Exception {
        new LongConnServer().serve(8888);
    }

    public void serve(int port) throws Exception {
        // accepts an incoming connection，and registers the accepted connection to the worker
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // handles the traffic of the accepted connection once the boss accepts the connection
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 发生在初始化的时候
//                    .handler()
                    // 发生在客户端连接之后，指定在接收到连接后，如何处理接收到的数据
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    // 在Netty中，添加自定义编解码器和自定义处理器的顺序需要特别注意。
                                    // 一般来说，编解码器应该放在处理器的前面。
                                    // 这是因为，在数据处理之前，需要先对数据进行解码，将二进制数据转换成Java对象；
                                    // 同样，在数据发送之前，需要将Java对象编码成二进制数据。
                                    .addLast(new LiveDecoder())
                                    .addLast(new StringToByteEncoder())
                                    .addLast(new LiveHandler());
                        }
                    })
                    // that accepts incoming connections
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // for the Channels accepted by the parent ServerChannel
                    // 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
            ChannelFuture f = b.bind(port).sync();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
