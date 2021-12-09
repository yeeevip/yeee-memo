package com.xiaoleilu.loServer.example;

import com.xiaoleilu.loServer.LoServer;
import com.xiaoleilu.loServer.ServerSetting;
import com.xiaoleilu.loServer.action.Action;
import com.xiaoleilu.loServer.filter.Filter;
import com.xiaoleilu.loServer.handler.Request;
import com.xiaoleilu.loServer.handler.Response;

/**
 * loServer样例程序<br>
 * Action对象用于处理业务流程，类似于Servlet对象<br>
 * 在启动服务器前必须将path和此Action加入到ServerSetting的ActionMap中<br>
 * 使用ServerSetting.setPort方法设置监听端口，此处设置为8090（如果不设置则使用默认的8090端口）
 * 然后调用LoServer.start()启动服务<br>
 * 在浏览器中访问http://localhost:8090/example?a=b既可在页面上显示response a: b
 * @author Looly
 *
 */
public class ExampleAction implements Action{

	@Override
	public void doAction(Request request, Response response) {
		String a = request.getParam("a");
		response.setContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+
			"<html>"+
					"<head>"+
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"+
						"<title>发起项目</title>"+
						"<script type=\"text/javascript\">alert('"+a+"');</script>"+
					"</head>"+
				"<body>"+
				"</body>"+
			"</html>");
		//throw new RuntimeException("Test");
	}

	public static void main(String[] args) {
		ServerSetting.setFilter("/*", new Filter() {
			
			@Override
			public boolean doFilter(Request request, Response response) {
				// TODO Auto-generated method stub
				if(request.getPath().equals("/example")){
					return true;
				}
				return false;
			}
		});
		ServerSetting.setAction("/example", ExampleAction.class);
		ServerSetting.setRoot("root");
		ServerSetting.setPort(8090);
		LoServer.start();
	}
}