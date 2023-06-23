package vip.yeee.memo.demo.jdk.base.io.nio.aio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/21 14:47
 */
public class HeartbeatClient {

    public static void main(String[] args) {
        HeartbeatClient client = new HeartbeatClient();
        client.start("localhost", 8081);
    }

    public void start(String hostname, Integer port) {
        try {
            AsynchronousSocketChannel clientSocket = AsynchronousSocketChannel.open();
            clientSocket.connect(new InetSocketAddress(hostname, port), null, new ConnectHandler(clientSocket, ByteBuffer.allocate(1024)));
            // 等待客户端退出
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConnectHandler implements CompletionHandler<Void, Void> {
        private final AsynchronousSocketChannel clientSocket;
        private final ByteBuffer buffer;

        public ConnectHandler(AsynchronousSocketChannel clientSocket, ByteBuffer buffer) {
            this.clientSocket = clientSocket;
            this.buffer = buffer;
        }

        @Override
        public void completed(Void result, Void attachment) {
            System.out.println("Connected to heartbeat server");
            buffer.put("heartbeat".getBytes());
            buffer.flip();
            clientSocket.write(buffer, buffer, new WriteHandler(clientSocket));
        }

        @Override
        public void failed(Throwable throwable, Void attachment) {
            throwable.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        private final AsynchronousSocketChannel clientSocket;

        public ReadHandler(AsynchronousSocketChannel clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void completed(Integer readBytes, ByteBuffer buffer) {
            if (readBytes == -1) {  // 服务端关闭连接
                System.out.println("Heartbeat server disconnected");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String message = new String(buffer.array(), 0, readBytes);
                if (message.trim().equals("heartbeat response")) {  // 收到心跳响应
                    System.out.println("Received heartbeat response");
                    try {
                        Thread.sleep(5000);  // 等待5秒钟，模拟下一次心跳间隔
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buffer.clear();
                    buffer.put("heartbeat".getBytes());
                    buffer.flip();
                    clientSocket.write(buffer, buffer, new WriteHandler(clientSocket));
                } else {  // 收到其他响应
                    System.out.println("Received message from server: " + message);
                }
            }
        }

        @Override
        public void failed(Throwable throwable, ByteBuffer attachment) {
            throwable.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        private final AsynchronousSocketChannel clientSocket;

        public WriteHandler(AsynchronousSocketChannel clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void completed(Integer writeBytes, ByteBuffer buffer) {
            if (buffer.hasRemaining()) {  // 写入未完成，继续写入
                clientSocket.write(buffer, buffer, this);
            } else {  // 写入完成，开始读取响应
                buffer.clear();
                clientSocket.read(buffer, buffer, new ReadHandler(clientSocket));
            }
        }

        @Override
        public void failed(Throwable throwable, ByteBuffer buffer) {
            throwable.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
