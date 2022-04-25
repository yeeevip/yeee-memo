package com.impl.test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/26 14:11
 */
public class TestNoBlockingNetNio {

    /*
     * 功能概述：
     * 1.客户端向服务端发送一个文件
     * 2.服务端收到文件存储在本地
     * 3.服务端响应message给客户端
     * 4.客户端收到响应的message打印
    **/
}

@Slf4j
class NoBlockClient {

    public static void main(String[] args) throws IOException {
        // 1.获得通道
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        // 1.1切换成非阻塞模式
        //clientChannel.configureBlocking(false);

        // 2.发送一张图片给服务器
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);

        log.info("client write data to server ！！！ ");
        // 写header （长度）
        ByteBuffer lengthBuf = ByteBuffer.allocate(4);
        lengthBuf.putInt((int) fileChannel.size());
        lengthBuf.flip();
        clientChannel.write(lengthBuf);

        // 写文件流
        ByteBuffer buf = ByteBuffer.allocate(10 * 1024);
        while (fileChannel.read(buf) != -1) {
            buf.flip();
            clientChannel.write(buf);
            buf.clear();
        }

        log.info("client get resp data from server");
        // 接收响应
        lengthBuf.clear();
        clientChannel.read(lengthBuf);

        lengthBuf.flip();

        ByteBuffer respData = ByteBuffer.allocate(lengthBuf.getInt());
        log.info("client received resp data length = " + respData.capacity());

        clientChannel.read(respData);
        respData.flip();
        log.info("client received resp data = " + new String(respData.array()));


        fileChannel.close();
        clientChannel.close();

    }
}

@Slf4j
class NoBlockServer {

    private static boolean readHead;
    private static ByteBuffer fileLengthBuf = ByteBuffer.allocate(4);
    private static ByteBuffer fileDataBuf = null;

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
        while (selector.select() > 0) {

            // 6.获取当前选择器所有注册的“选择键”（已就绪的事件）
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {

                SelectionKey selectionKey = keyIterator.next();

                if (selectionKey.isValid()) {
                    handleKey(serverSocketChannel, selector, selectionKey);
                    keyIterator.remove();
                }

            }
        }

    }

    private static void handleKey(ServerSocketChannel serverSocketChannel, Selector selector, SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) { // 有新的socket链接进来

            // 获取客户端的连接
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            // 注册到选择器上-->拿到客户端的连接为了读取通道的数据（监听读就绪事件）
            client.register(selector, SelectionKey.OP_READ);

            //keyIterator.remove();

        }
        if (selectionKey.isReadable()) { // 读事件就绪

            // 获取当前选择器读事件就绪的通道
            SocketChannel client = (SocketChannel) selectionKey.channel();

            if (!readHead) {
                client.read(fileLengthBuf);
                readHead = true;
            } else {

                if (fileDataBuf == null) {
                    fileLengthBuf.flip();
                    int fileLength = fileLengthBuf.asIntBuffer().get();

                    log.info("server received data length = " + fileLength);

                    if (fileLength <= 0) return;
                    fileDataBuf = ByteBuffer.allocate(fileLength);
                }

                int count = client.read(fileDataBuf);

                log.info("server received seg data to buffer -- count = " + count);

                if (count >= 0 && fileDataBuf.remaining() == 0) {

                    log.info("server received seg data to buffer completed ！！！");

                    FileChannel saveFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证1111111111.jpg")
                            , StandardOpenOption.CREATE, StandardOpenOption.WRITE);

                    fileDataBuf.flip();

                    saveFileChannel.write(fileDataBuf);
                    saveFileChannel.close();

                    fileDataBuf.clear();
                    fileDataBuf = null;
                    readHead = false;
                    fileLengthBuf.clear();

                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }

            }
        }

        if (selectionKey.isWritable()) {

            SocketChannel client = (SocketChannel) selectionKey.channel();

            log.info("server response data to client");

            try {

                byte[] resp = ("收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了收到了" +
                        "收到了收到了！").getBytes(StandardCharsets.UTF_8);

                ByteBuffer respBuf = ByteBuffer.allocate(4 + resp.length);
                respBuf.putInt(resp.length);
                respBuf.put(resp);

                respBuf.flip();

                client.write(respBuf);

                selectionKey.interestOps(0);

            } catch (IOException e) {
                e.printStackTrace();
                client.socket().close();
                client.close();
            }
        }
    }

}