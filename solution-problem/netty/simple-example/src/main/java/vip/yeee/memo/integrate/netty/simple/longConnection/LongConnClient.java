package vip.yeee.memo.integrate.netty.simple.longConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 * @author yeeee
 * @since 2021/12/15 11:13
 */
public class LongConnClient {

    private final static Logger logger = LoggerFactory.getLogger(LongConnClient.class);

    String host = "localhost";
    int port = 8888;

    public static void main(String[] args) throws Exception {
        new LongConnClient().testLongConn();
    }

    public void testLongConn() throws Exception {
        logger.debug("start");
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            while (true) {
                try {
                    byte[] input = new byte[64];
                    int readByte = socket.getInputStream().read(input);
                    if (readByte >= 0) {
                        logger.debug(new String(input));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int code;
        while (true) {
            code = scanner.nextInt();
            logger.debug("input code: " + code);
            if (code == 0) {
                break;
            } else if (code == 1) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(5);
                byteBuffer.put((byte) 1);
                byteBuffer.putInt(0);
                socket.getOutputStream().write(byteBuffer.array());
                logger.debug("write heart finish! ");
            } else if (code == 2) {
                byte[] content = ("hello，I'm" +hashCode()).getBytes(StandardCharsets.UTF_8);
                ByteBuffer byteBuffer = ByteBuffer.allocate(content.length + 5);
                byteBuffer.put((byte) 2);
                byteBuffer.putInt(content.length);
                byteBuffer.put(content);
                socket.getOutputStream().write(byteBuffer.array());
                logger.debug("write content finish! ");
            }
        }
        socket.close();
    }

}
