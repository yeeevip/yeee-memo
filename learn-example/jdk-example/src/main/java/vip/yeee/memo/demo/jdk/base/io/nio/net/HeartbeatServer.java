package vip.yeee.memo.demo.jdk.base.io.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * JDK1.4非阻塞IO-多路复用器-网络
 * @author yeeee
 * @since 2021/12/14 15:29
 */
public class HeartbeatServer {

    public static void main(String[] args) {
        HeartbeatServer server = new HeartbeatServer();
        server.start(8080);
    }

    private Selector selector;

    public void start(int port) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server started...");

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(key -> {
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                });
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("client connected: " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = socketChannel.read(buffer);
            if (len == -1) {
                disconnect(key);
                return;
            }
            if (len > 0) {
                buffer.flip();
                byte[] bytes = new byte[len];
                buffer.get(bytes);
                String message = new String(bytes);
                System.out.println("received message: " + message.trim());
            }
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(key);
        }
    }

    private void write(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            String message = "heartbeat response";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            int len = socketChannel.write(buffer);
            if (len == -1) {
                disconnect(key);
                return;
            }
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(key);
        }
    }

    private void disconnect(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            System.out.println("client disconnected: " + socketChannel.getRemoteAddress());
            socketChannel.close();
            key.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}