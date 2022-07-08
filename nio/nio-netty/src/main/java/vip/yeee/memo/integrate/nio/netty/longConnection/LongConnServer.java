package vip.yeee.memo.integrate.nio.netty.longConnection;

import vip.yeee.memo.integrate.nio.netty.decoder.LiveDecoder;
import vip.yeee.memo.integrate.nio.netty.encoder.StringToByteEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/15 11:40
 */
public class LongConnServer {

    public void serve(int port) throws Exception {
        // accepts an incoming connection
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // handles the traffic of the accepted connection once the boss accepts the connection
        // and registers the accepted connection to the worker
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
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
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new LongConnServer().serve(8888);
    }

}
