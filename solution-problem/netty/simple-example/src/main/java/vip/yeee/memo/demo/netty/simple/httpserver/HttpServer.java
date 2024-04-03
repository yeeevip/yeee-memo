package vip.yeee.memo.demo.netty.simple.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * mvn compile、mvn exec:java -Dexec.mainClass="com.netty.learn.httpserver.HttpServer" -Dexec.args="arg0 arg1 arg2"
 * @author https://www.yeee.vip
 * @since 2021/12/14 16:31
 */
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage：" + HttpServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new HttpServer(port).start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            System.out.println("initChannel channel: " + channel);
                            channel.pipeline()
                                    .addLast("decoder", new HttpRequestDecoder())  // 用于解码request
                                    .addLast("encoder", new HttpResponseEncoder()) // 用于编码response
                                    // 消息聚合器为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，
                                    // 如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse
                                    .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                    .addLast(new HttpHandler()); // 添加我们自己的处理接口
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
