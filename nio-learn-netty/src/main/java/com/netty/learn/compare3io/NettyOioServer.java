package com.netty.learn.compare3io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @Description: Netty
 * @Author: yeeeeee
 * @Date: 2021/12/14 15:47
 */
public class NettyOioServer {

    public static void main(String[] args) throws Exception {
        NettyOioServer nettyOioServer = new NettyOioServer();
        nettyOioServer.serve(8080);
    }

    public void serve(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi！\r\n", StandardCharsets.UTF_8));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();              // 1
            b.group(group)                                          // 2
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 3
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter() {     // 4
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE); // 5
                                        }
                                    });
                        }
                    });
            ChannelFuture f = b.bind().sync();              // 6
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();              // 7
        }
    }

}
