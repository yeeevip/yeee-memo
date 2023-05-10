package vip.yeee.memo.integrate.netty.heartcheck.client.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Command;
import vip.yeee.memo.integrate.netty.heartcheck.common.protobuf.Message;

public class AuthHandler extends SimpleChannelInboundHandler<Message> {

	public Logger log = LoggerFactory.getLogger(this.getClass());

	private final static String CLIENTID = "test";

	// 连接成功后，向server发送消息  
	@Override  
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Message.MessageBase.Builder authMsg = Message.MessageBase.newBuilder();
		authMsg.setClientId(CLIENTID);
		authMsg.setCmd(Command.CommandType.AUTH);
		authMsg.setData("This is auth data");
		ctx.writeAndFlush(authMsg.build());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.debug("连接断开");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

	}
}
