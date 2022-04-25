package com.impl.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/26 11:52
 */
public class TestBlockingNetNio {


}


class BlockClient {

    public static void main(String[] args) throws IOException {

        // 1. 获得通道
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        // 2.发送一张图片给服务器
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(11024);

        while (fileChannel.read(buf) != -1) {
            // 在读之前切换成读模式
            buf.flip();
            clientChannel.write(buf);
            // 读之后切换为写模式
            buf.clear();
        }

        // 告诉服务器已经写完了，不然会一直阻塞
        clientChannel.shutdownOutput();

        // 3.接收应答
        int len = 0;
        while ((len = clientChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

        clientChannel.close();
        fileChannel.close();
    }

}

class BlockServer {
    public static void main(String[] args) throws IOException {

        // 1.获得通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证1111111111.jpg"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        // 3.绑定连接
        serverChannel.bind(new InetSocketAddress(6666));

        // 4.获取客户端的链接（阻塞的）
        SocketChannel client = serverChannel.accept();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (client.read(buf) != -1) {
            buf.flip();
            fileChannel.write(buf);
            buf.clear();
        }

        // 5.应答
        buf.put("img has received！".getBytes(StandardCharsets.UTF_8));
        buf.flip();
        client.write(buf);
        buf.clear();

        fileChannel.close();
        serverChannel.close();
        client.close();

    }
}