package vip.yeee.memo.integrate.jdk.base.io.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * description......
 * @author yeeee
 * @since 2021/12/14 15:15
 */
public class HeartbeatClient {

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            while (!socketChannel.finishConnect()) {
                // waiting for connection to complete
            }
            System.out.println("Connected to heartbeat server on " + socketChannel.getRemoteAddress());

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                String message = "heartbeat";
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);
                System.out.println("Sent heartbeat message: " + message.trim());

                buffer.clear();
                int readBytes = socketChannel.read(buffer);
                if (readBytes == -1) {  // 服务端关闭连接
                    System.out.println("Heartbeat server disconnected");
                    break;
                } else {
                    String response = new String(buffer.array(), 0, readBytes);
                    if (response.equals("heartbeat response")) {  // 收到心跳响应
                        System.out.println("Received heartbeat response");
                        try {
                            Thread.sleep(5000);  // 等待5秒钟，模拟下一次心跳间隔
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {  // 收到非心跳响应
                        System.out.println("Received message from server: " + response.trim());
                    }
                }

                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


