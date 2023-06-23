package vip.yeee.memo.demo.jdk.base.io.oio.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/21 14:45
 */
public class HeartbeatServer {

    public static void main(String[] args) throws IOException {
        HeartbeatServer server = new HeartbeatServer();
        server.start(8081);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Heartbeat server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                new Thread(() -> {
                    try (InputStream in = clientSocket.getInputStream();
                         OutputStream out = clientSocket.getOutputStream()) {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int readBytes = in.read(buffer);
                            if (readBytes == -1) {  // 客户端关闭连接
                                System.out.println("Client disconnected from " + clientSocket.getRemoteSocketAddress());
                                clientSocket.close();
                                break;
                            }
                            String message = new String(buffer, 0, readBytes);
                            if (message.equals("heartbeat")) {  // 收到心跳包
                                System.out.println("Received heartbeat message from " + clientSocket.getRemoteSocketAddress());
                                out.write("heartbeat response".getBytes());  // 响应心跳
                                out.flush();
                            } else {  // 收到非心跳包
                                System.out.println("Received message from " + clientSocket.getRemoteSocketAddress() + ": " + message);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
