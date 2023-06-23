package vip.yeee.memo.demo.netty.heartcheck.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import vip.yeee.memo.demo.netty.heartcheck.server.typeServer.TCPServer;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务器启动类
*/
@SpringBootApplication
public class HeartCheckServerApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(HeartCheckServerApplication.class, args);
		TCPServer tcpServer = context.getBean(TCPServer.class);
		openBrowse("http://localhost:10003/monitor.do");
		tcpServer.start();
	}

	/**
	 * 使用默认浏览器打开
	 */
	private static void openBrowse(String url) throws Exception {
		List<String> list = new ArrayList<>();
		list.add("cmd");
		list.add("/c");
		list.add("start");
		list.add(url);
		Runtime.getRuntime().exec(list.toArray(new String[0]));
	}

}