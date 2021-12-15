package com.netty.learn.longConnection;

import com.netty.learn.decoder.LiveDecoder;
import com.netty.learn.encoder.ShortToByteEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
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
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast("decoder", new LiveDecoder())
                                .addLast("encoder", new ShortToByteEncoder())
                                .addLast("handler", new LiveHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        b.bind(port).sync();
    }

    public static void main(String[] args) throws Exception {
        new LongConnServer().serve(8888);
    }

}
