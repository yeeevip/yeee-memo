package vip.yeee.memo.integrate.netty.heartcheck.client.heart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.yeee.memo.integrate.netty.heartcheck.client.handler.AuthHandler;
import vip.yeee.memo.integrate.netty.heartcheck.client.handler.HeartHandler;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Message;

import java.util.concurrent.TimeUnit;

/**
 * netty客户端
 */
public class NettyClient {

	public Logger log = LoggerFactory.getLogger(this.getClass());

	private final static String HOST = "127.0.0.1";
	private final static int PORT = 19999;
	private final static int READER_IDLE_TIME_SECONDS = 0;//读操作空闲20秒
	private final static int WRITER_IDLE_TIME_SECONDS = 5;//写操作空闲20秒
	private final static int ALL_IDLE_TIME_SECONDS = 0;//读写全部空闲40秒

	private final EventLoopGroup loop = new NioEventLoopGroup();

	public void run() throws Exception {
		try {
			doConnect(new Bootstrap(), loop);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * netty client 连接，连接失败5秒后重试连接
	 */
	public Bootstrap doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
		try {
			if (bootstrap != null) {
				bootstrap.group(eventLoopGroup);
				bootstrap.channel(NioSocketChannel.class);
				bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
				bootstrap.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS
								, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS, TimeUnit.SECONDS));

						p.addLast(new ProtobufVarint32FrameDecoder());
						p.addLast(new ProtobufDecoder(Message.MessageBase.getDefaultInstance()));

						p.addLast(new ProtobufVarint32LengthFieldPrepender());
						p.addLast(new ProtobufEncoder());

						p.addLast("authHandler", new AuthHandler());
						p.addLast("heartHandler", new HeartHandler(NettyClient.this));
					}
				});
				bootstrap.remoteAddress(HOST, PORT);
				ChannelFuture f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
					final EventLoop eventLoop = futureListener.channel().eventLoop();
					if (!futureListener.isSuccess()) {
						log.warn("连接服务器失败，5s后重新尝试连接！");
						futureListener.channel().eventLoop().schedule(() -> doConnect(new Bootstrap(), eventLoop), 5, TimeUnit.SECONDS);
					}
				});
				f.channel().closeFuture().sync();
				//eventLoopGroup.shutdownGracefully();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return bootstrap;
	}
}
