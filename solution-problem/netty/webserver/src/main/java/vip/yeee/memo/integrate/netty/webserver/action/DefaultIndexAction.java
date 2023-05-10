package vip.yeee.memo.integrate.netty.webserver.action;

import vip.yeee.memo.integrate.netty.webserver.handler.Request;
import vip.yeee.memo.integrate.netty.webserver.handler.Response;

/**
 * 默认的主页Action，当访问主页且没有定义主页Action时，调用此Action
 * @author Looly
 *
 */
public class DefaultIndexAction implements Action{

	@Override
	public void doAction(Request request, Response response) {
		response.setContent("Welcome to LoServer.");
	}

}