package vip.yeee.memo.integrate.nio.jdk;

import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/24 10:46
 */
@Data
public class ProxyHandler {
    private String method;
    private String host;
    private int port;
    private SocketChannel remoteServer;
    private SocketChannel remoteClient;

    /**
     * 原始信息
     */
    private List<ByteBuffer> buffers = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();

    /**
     * 连接到远程
     * @param remoteClient
     * @return
     * @throws IOException
     */
    public SocketChannel proxy(SocketChannel remoteClient) throws IOException {
        this.remoteClient = remoteClient;
        connect();
        return this.remoteServer;
    }

    public void connect() throws IOException {
        // 解析METHOD, HOST和PORT
        beforeConnected();

        // 链接REMOTE SERVER
        createRemoteServer();

        // CONNECT请求回应，其他请求WRITE THROUGH
        afterConnected();
    }

    protected void beforeConnected() throws IOException {
        // 读取HEADER
        readAllHeader();

        // 解析HOST和PORT
        parseRemoteHostAndPort();
    }

    /**
     * 创建远程连接
     * @throws IOException
     */
    protected void createRemoteServer() throws IOException {
        remoteServer = SocketChannel.open(new InetSocketAddress(host, port));
    }

    /**
     * 连接建立后预处理
     * @throws IOException
     */
    protected void afterConnected() throws IOException {
        // 当CONNECT请求时，默认写入200到CLIENT
        if ("CONNECT".equalsIgnoreCase(method)) {
            // CONNECT默认为443端口，根据HOST再解析
            remoteClient.write(ByteBuffer.wrap("HTTP/1.0 200 Connection Established\r\nProxy-agent: nginx\r\n\r\n".getBytes()));
        } else {
            writeThrough();
        }
    }

    protected void writeThrough() {
        buffers.forEach(byteBuffer -> {
            try {
                remoteServer.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 读取请求内容
     * @throws IOException
     */
    protected void readAllHeader() throws IOException {
        while (true) {
            ByteBuffer clientBuffer = newByteBuffer();
            int read = remoteClient.read(clientBuffer);
            clientBuffer.flip();
            appendClientBuffer(clientBuffer);
            if (read < clientBuffer.capacity()) {
                break;
            }
        }
    }

    /**
     * 解析出HOST和PORT
     * @throws IOException
     */
    protected void parseRemoteHostAndPort() throws IOException {
        // 读取第一批，获取到METHOD
        method = parseRequestMethod(stringBuilder.toString());

        // 默认为80端口，根据HOST再解析
        port = 80;
        if ("CONNECT".equalsIgnoreCase(method)) {
            port = 443;
        }

        this.host = parseHost(stringBuilder.toString());

        URI remoteServerURI = URI.create(host);
        host = remoteServerURI.getHost();

        if (remoteServerURI.getPort() > 0) {
            port = remoteServerURI.getPort();
        }
    }

    protected void appendClientBuffer(ByteBuffer clientBuffer) {
        buffers.add(clientBuffer);
        stringBuilder.append(new String(clientBuffer.array(), clientBuffer.position(), clientBuffer.limit()));
    }

    protected static ByteBuffer newByteBuffer() {
        // buffer必须大于7，保证能读到method
        return ByteBuffer.allocate(128);
    }

    private static String parseRequestMethod(String rawContent) {
        // create uri
        return rawContent.split("\r\n")[0].split(" ")[0];
    }

    private static String parseHost(String rawContent) {
        String[] headers = rawContent.split("\r\n");
        String host = "host:";
        for (String header : headers) {
            if (header.length() > host.length()) {
                String key = header.substring(0, host.length());
                String value = header.substring(host.length()).trim();
                if (host.equalsIgnoreCase(key)) {
                    if (!value.startsWith("http://") && !value.startsWith("https://")) {
                        value = "http://" + value;
                    }
                    return value;
                }
            }
        }
        return "";
    }

}