package vip.yeee.memo.integrate.nio.netty.compare3io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/14 15:15
 */
public class TestClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8080));
        ByteBuffer resBuf = ByteBuffer.allocate(32);
        socketChannel.read(resBuf);
        System.out.println("received resp msg = " + new String(resBuf.array()));
        socketChannel.close();
    }

}


