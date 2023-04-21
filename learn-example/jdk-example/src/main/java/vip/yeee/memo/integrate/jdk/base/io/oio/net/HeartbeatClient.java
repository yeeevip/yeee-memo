package vip.yeee.memo.integrate.jdk.base.io.oio.net;

import java.io.*;
import java.net.Socket;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/21 14:47
 */
public class HeartbeatClient {

    public static void main(String[] args) {
        String hostname = "localhost";  // 或者替换成服务端所在机器的IP地址
        int port = 8081;
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to heartbeat server on " + hostname + ":" + port);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                out.write("heartbeat".getBytes());  // 发送心跳包
                out.flush();
                int readBytes = in.read(buffer);  // 等待心跳响应
                if (readBytes == -1) {  // 服务端关闭连接
                    System.out.println("Heartbeat server disconnected");
                    break;
                } else {
                    String message = new String(buffer, 0, readBytes);
                    if (message.equals("heartbeat response")) {  // 收到心跳响应
                        System.out.println("Received heartbeat response");
                        try {
                            Thread.sleep(5000);  // 等待5秒钟，模拟下一次心跳间隔
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {  // 收到其它响应
                        System.out.println("Received message from server: " + message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
