package vip.yeee.memo.integrate.nio.jdk.ssl;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/24 17:57
 */
@Slf4j
public class NioSslClient {

    public static void main(String[] args) throws Exception {
        NioSslClient sslClient = new NioSslClient("httpbin.org", 443);
        sslClient.connect();
        // 请求 'https://httpbin.org/get'
    }

    private String remoteAddress;

    private int port;

    private SSLEngine engine;

    private SocketChannel socketChannel;

    private SSLContext context;

    /**
     * 需要远程的HOST和PORT
     * @param remoteAddress
     * @param port
     * @throws Exception
     */
    public NioSslClient(String remoteAddress, int port) throws Exception {
        this.remoteAddress = remoteAddress;
        this.port = port;

        //context = clientSSLContext();
        engine = context.createSSLEngine(remoteAddress, port);
        engine.setUseClientMode(true);
    }

    public boolean connect() throws Exception {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(remoteAddress, port));
        while (!socketChannel.finishConnect()) {
            // 通过REACTOR，不会出现等待情况
            //log.debug("连接中..");
        }

        SslSocketChannel sslSocketChannel = new SslSocketChannel(context, socketChannel, true);
        sslSocketChannel.doHandshake();

        // 握手完成后，开启SELECTOR
        Selector selector = SelectorProvider.provider().openSelector();
        socketChannel.register(selector, SelectionKey.OP_READ, sslSocketChannel);

        // 写入请求
        sslSocketChannel.write("GET /get HTTP/1.1\r\n"
                + "Host: httpbin.org:443\r\n"
                + "User-Agent: curl/7.62.0\r\n"
                + "Accept: */*\r\n"
                + "\r\n");

        // 读取结果
        while (true) {
            selector.select();
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                selectedKeys.remove();
                if (key.isValid() && key.isReadable()) {
                    ((SslSocketChannel)key.attachment()).read(buf->{
                        log.info("{}", new String(buf.array(), 0, buf.position()));
                    });
                    ((SslSocketChannel)key.attachment()).closeConnection();
                    return true;
                }
            }
        }
    }
}