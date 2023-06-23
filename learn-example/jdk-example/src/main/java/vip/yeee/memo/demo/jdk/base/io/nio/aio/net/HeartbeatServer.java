package vip.yeee.memo.demo.jdk.base.io.nio.aio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * JDK1.7异步IO-网络
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
        try {
            AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("Heartbeat server started on port " + port);
            serverSocket.accept(null, new AcceptHandler(serverSocket));
            // 等待服务器退出
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private final AsynchronousServerSocketChannel serverSocket;
        public AcceptHandler(AsynchronousServerSocketChannel serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void completed(AsynchronousSocketChannel clientSocket, Void attachment) {
            try {
                System.out.println("Client connected from " + clientSocket.getRemoteAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            clientSocket.read(buffer, buffer, new HeartbeatServer.ReadHandler(clientSocket));
            serverSocket.accept(null, this);
        }

        @Override
        public void failed(Throwable throwable, Void attachment) {
            if (throwable instanceof AsynchronousCloseException) {
                System.out.println("Heartbeat server stopped");
            } else {
                throwable.printStackTrace();
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
            if (readBytes == -1) {  // 客户端关闭连接
                try {
                    System.out.println("Client disconnected from " + clientSocket.getRemoteAddress());
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String message = new String(buffer.array(), 0, readBytes);
                if (message.trim().equals("heartbeat")) {  // 收到心跳包
                    try {
                        System.out.println("Received heartbeat message from " + clientSocket.getRemoteAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteBuffer response = ByteBuffer.wrap("heartbeat response\r\n".getBytes());
                    clientSocket.write(response, response, new WriteHandler(clientSocket));
                } else {  // 收到非心跳包
                    try {
                        System.out.println("Received message from " + clientSocket.getRemoteAddress() + ": " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteBuffer response = ByteBuffer.wrap("message received\r\n".getBytes());
                    clientSocket.write(response, response, new WriteHandler(clientSocket));
                }
            }
        }

        @Override
        public void failed(Throwable throwable, ByteBuffer attachment) {
            try {
                System.out.println("Client disconnected from " + clientSocket.getRemoteAddress());
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
            } else {  // 写入完成，继续读取下一条消息
                buffer.clear();
                clientSocket.read(buffer, buffer, new HeartbeatServer.ReadHandler(clientSocket));
            }
        }

        @Override
        public void failed(Throwable throwable, ByteBuffer attachment) {
            try {
                System.out.println("Client disconnected from " + clientSocket.getRemoteAddress());
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
