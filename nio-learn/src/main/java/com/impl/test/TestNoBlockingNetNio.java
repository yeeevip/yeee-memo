package com.impl.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/11/26 14:11
 */
public class TestNoBlockingNetNio {
}


class NoBlockClient {

    public static void main(String[] args) throws IOException {
        // 1.获得通道
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        // 1.1切换成非阻塞模式
        clientChannel.configureBlocking(false);

      /*  Selector selector = Selector.open();

        clientChannel.register(selector, SelectionKey.OP_READ);*/

        // 2.发送一张图片给服务器
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (fileChannel.read(buf) != -1) {
            buf.flip();
            clientChannel.write(buf);
            buf.clear();
        }

        fileChannel.close();
        clientChannel.close();
/*
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    {
                        int len = 0;
                        while ((len = channel.read(buf)) > 0) {
                            buf.flip();
                            System.out.println(new String(buf.array(), 0, len));
                            buf.clear();
                        }
                        //clientChannel.close();
                    }

                }
                iterator.remove();
            }
        }*/



    }
}

class NoBlockServer {

    public static void main(String[] args) throws IOException {

        // 1.获得通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 1.1切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 4.获取选择器
        Selector selector = Selector.open();

        // 4.1将通道注册到选择器上，指定接收“监听通道”事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 5.轮询的获取选择器上已“就绪”的事件 -- > 只要selector > 0 说明已就绪
        while (selector.select() >  0) {

            // 6.获取当前选择器所有注册的“选择键”（已就绪的事件）
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {

                SelectionKey selectionKey = keyIterator.next();

                if (selectionKey.isAcceptable()) { // 接收事件就绪

                    // 获取客户端的连接
                    SocketChannel client = serverSocketChannel.accept();
                    client.configureBlocking(false);
                    // 注册到选择器上-->拿到客户端的连接为了读取通道的数据（监听读就绪事件）
                    client.register(selector, SelectionKey.OP_READ);

                }
                if (selectionKey.isReadable()) { // 读事件就绪

                    // 获取当前选择器读事件就绪的通道
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    {
                        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证1111111111.jpg"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                        ByteBuffer fileChannelBuf = ByteBuffer.allocate(1024);
                        while (client.read(fileChannelBuf) != -1) {
                            fileChannelBuf.flip();
                            fileChannel.write(fileChannelBuf);
                            fileChannelBuf.clear();
                        }
                        fileChannel.close();
                    }

        /*            {
                        ByteBuffer responseBuf = ByteBuffer.allocate(1024);
                        responseBuf.put("收到了！".getBytes(StandardCharsets.UTF_8));
                        responseBuf.flip();
                        client.write(responseBuf);
                        responseBuf.clear();
                    }*/


                }

                // 从选择器中取消已处理过的事件
                keyIterator.remove();

            }
        }

    }

}