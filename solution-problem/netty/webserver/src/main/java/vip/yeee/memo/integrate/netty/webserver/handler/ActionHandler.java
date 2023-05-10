package vip.yeee.memo.integrate.netty.webserver.handler;

import java.io.IOException;

import cn.hutool.core.lang.Singleton;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import vip.yeee.memo.integrate.netty.webserver.ServerSetting;
import vip.yeee.memo.integrate.netty.webserver.action.Action;
import vip.yeee.memo.integrate.netty.webserver.action.ErrorAction;
import vip.yeee.memo.integrate.netty.webserver.action.FileAction;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import vip.yeee.memo.integrate.netty.webserver.filter.Filter;

/**
 * Action处理单元
 * 
 * @author Looly
 */
public class ActionHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final Log log = LogFactory.get();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {

		final Request request = Request.build(ctx, fullHttpRequest);
		final Response response = Response.build(ctx, request);
		
		try {
			//do filter
			boolean isPass = this.doFilter(request, response);
			
			if(isPass){
				//do action
				this.doAction(request, response);
			}
		} catch (Exception e) {
			Action errorAction = ServerSetting.getAction(ServerSetting.MAPPING_ERROR);
			request.putParam(ErrorAction.ERROR_PARAM_NAME, e);
			errorAction.doAction(request, response);
		}
		
		//如果发送请求未被触发，则触发之，否则跳过。
		if(!response.isSent()){
			response.send();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof IOException){
			log.warn("{}", cause.getMessage());
		}else{
			super.exceptionCaught(ctx, cause);
		}
	}
	
	//---------------------------------------------------------------------------------------- Private method start
	/**
	 * 执行过滤
	 * @param request 请求
	 * @param response 响应
	 * @param 是否继续
	 */
	private boolean doFilter(Request request, Response response) {
		//全局过滤器
		Filter filter = ServerSetting.getFilter(ServerSetting.MAPPING_ALL);
		if(null != filter){
			if(!filter.doFilter(request, response)){
				return false;
			}
		}
		
		//自定义Path过滤器
		filter = ServerSetting.getFilter(request.getPath());
		if(null != filter){
			return filter.doFilter(request, response);
		}
		
		return true;
	}
	
	/**
	 * 执行Action
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	private void doAction(Request request, Response response){
		Action action = ServerSetting.getAction(request.getPath());
		if (null == action) {
			//查找匹配所有路径的Action
			action = ServerSetting.getAction(ServerSetting.MAPPING_ALL);
			if(null == action){
				// 非Action方法，调用静态文件读取
				action = Singleton.get(FileAction.class);
			}
		}

		action.doAction(request, response);
	}
	//---------------------------------------------------------------------------------------- Private method start
}
