package vip.yeee.memo.integrate.netty.heartcheck.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Command;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Message;
import vip.yeee.memo.integrate.netty.heartcheck.server.components.ChannelRepository;

/**
* 实现心跳的handler  支持超时断开客户端避免浪费资源
*/
@Component(value = "serverHeartHandler")
// 这个注解意味着一个 ChannelHandler 可以被多个 Channel 安全地共享，并且不需要每个 Channel 都创建一个新的 ChannelHandler 实例。这通常在一些应用场景下非常有用，比如需要对多个连接进行相同的数据处理操作。
@ChannelHandler.Sharable
public class ServerHeartHandler extends ChannelInboundHandlerAdapter {

	public Logger log = LoggerFactory.getLogger(this.getClass());

	private final AttributeKey<String> clientInfo = AttributeKey.valueOf("clientInfo");
	// 设置6秒检测chanel是否接受过心跳数据
	private static final int READ_WAIT_SECONDS = 6;

	// 定义客户端没有收到服务端的pong消息的最大次数
	private static final int MAX_UN_REC_PING_TIMES = 3;

	// 失败计数器：未收到client端发送的ping请求
	private int unRecPingTimes = 0 ;

	@Autowired
	@Qualifier("channelRepository")
	private ChannelRepository channelRepository;

	/*空闲触发器 心跳基于空闲实现*/
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (IdleState.READER_IDLE.equals(event.state())) {
				type = "read idle";
			} else if (IdleState.WRITER_IDLE.equals(event.state())) {
				type = "write idle";
			} else if (IdleState.ALL_IDLE.equals(event.state())) {
				type = "all idle";
			}
			if (unRecPingTimes >= MAX_UN_REC_PING_TIMES) {
				// 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
				ctx.channel().close();
			} else {
				// 失败计数器加1
				unRecPingTimes++;
			}
			log.debug(ctx.channel().remoteAddress() + "超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message.MessageBase msgBase = (Message.MessageBase)msg;
		String clientId = msgBase.getClientId();
		log.info(msgBase.getData());
		Channel ch = channelRepository.get(clientId);
		if (null == ch) {
			ch = ctx.channel();
			channelRepository.put(clientId, ch);
		}
		/*认证处理*/
		if (Command.CommandType.AUTH.equals(msgBase.getCmd())) {
			log.info("收到客户端Id是" + clientId + "的建立连接请求...");
			Attribute<String> attr = ctx.attr(clientInfo);
			attr.set(clientId);
			channelRepository.put(clientId, ctx.channel());
			ctx.writeAndFlush(createData(clientId, Command.CommandType.AUTH_BACK, "客户端Id是" + clientId + "的建立连接请求success！").build());
		} else if (Command.CommandType.PING.equals(msgBase.getCmd())) {
			//处理ping消息
			ctx.writeAndFlush(createData(clientId, Command.CommandType.PONG, "服务器响应客户端Id是" + clientId + "的心跳包").build());
			unRecPingTimes = 0;
		} else {
			if (ch.isOpen()) {
				//触发下一个handler
				ctx.fireChannelRead(msg);
			}
		}
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Attribute<String> attr = ctx.attr(clientInfo);
		String clientId = attr.get();
		log.error("客户端断开链接，Id是 " + clientId + "----错误详情是======" + cause.getMessage());
	}

	/*
	 * 构建不同类型的数据 基于protobuf
	 */
	private Message.MessageBase.Builder createData(String clientId, Command.CommandType cmd, String data) {
		Message.MessageBase.Builder msg = Message.MessageBase.newBuilder();
		msg.setClientId(clientId);
		msg.setCmd(cmd);
		msg.setData(data);
		return msg;
	}
}
