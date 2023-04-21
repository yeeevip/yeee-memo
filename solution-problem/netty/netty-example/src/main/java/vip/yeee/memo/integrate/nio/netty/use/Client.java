package vip.yeee.memo.integrate.nio.netty.use;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * description......
 * @author yeeee
 * @since 2021/12/14 15:15
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(8080));
        ByteBuffer sendBuf = ByteBuffer.wrap("Hello!!!".getBytes());
        socketChannel.write(sendBuf);
        ByteBuffer resBuf = ByteBuffer.allocate(32);
        socketChannel.read(resBuf);
        System.out.println("收到服务端消息：" + new String(resBuf.array()));
        socketChannel.close();
    }

}


