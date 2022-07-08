package vip.yeee.memo.integrate.nio.netty.compare3io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 非阻塞IO
 * @Author: yeeeeee
 * @Date: 2021/12/14 15:29
 */
public class PlainNioServer {

    public static void main(String[] args) throws IOException {
        PlainNioServer plainNioServer = new PlainNioServer();
        plainNioServer.serve(8080);
    }

    public void serve(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));              // 1
        Selector selector = Selector.open();                                // 2
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);     // 3
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes(StandardCharsets.UTF_8));
        for (;;) {
            try {
                selector.select();                                          // 4
            } catch (IOException e) {
                e.printStackTrace();
                // handle exception
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();          // 5
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {                               // 6
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector
                                , SelectionKey.OP_WRITE | SelectionKey.OP_READ
                                , msg.duplicate());                                        // 7
                        System.out.println("Accepted connection from " + client);
                    }
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        // 在关闭时忽略
                    }
                }
            }
        }
    }

}
