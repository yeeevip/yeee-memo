package vip.yeee.memo.integrate.nio.jdk.chartapp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/7 10:13
 */
@Slf4j
public class ChatClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private static SocketChannel socketChannel;
    private static ChatClient chatClient;

    private static final byte[] lock = new byte[1];

    private ChatClient() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
    }

    public static ChatClient getInstance() {
        if (chatClient != null) {
            return chatClient;
        }
        synchronized (lock) {
            if (chatClient != null) {
                return chatClient;
            }
            try {
                chatClient = new ChatClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return chatClient;
        }
    }

    public void close() {
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void sendMsg(String msg) {
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMsg() {
        String msg = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = socketChannel.read(buffer)) > 0) {
                sb.append(new String(buffer.array(), 0, len));
            }

            if (sb.length() > 0) {
                msg = sb.toString();
                if (sb.toString().equals("close")) {
                    //不过不sleep会导致ioException的发生,因为如果这里直接关闭掉通道，在server里，
                    //该channel在read（buffer）时会发生读取异常，通过sleep一段时间，使得服务端那边的channel先关闭，客户端
                    //的channel后关闭，这样就能防止read(buffer)的ioException
                    //但是这是一种笨方法
                    //Thread.sleep(100);
                    //更好的方法是，在readBuffer中捕获异常后，手动进行关闭通道
                    socketChannel.socket().close();
                    socketChannel.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

}
