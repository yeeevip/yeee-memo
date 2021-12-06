package com.impl.ssl;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/11/24 16:47
 */
@Slf4j
public class SslSocketChannel {

    /**
     * 握手加解密需要的四个存储
     */
    protected ByteBuffer myAppData; // 明文
    protected ByteBuffer myNetData; // 密文
    protected ByteBuffer peerAppData; // 明文
    protected ByteBuffer peerNetData; // 密文

    /**
     * 握手加解密过程中用到的异步执行器
     */
    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 原NIO 的 CHANNEL
     */
    protected SocketChannel socketChannel;

    /**
     * SSL 引擎
     */
    protected SSLEngine engine;

    public SslSocketChannel(SSLContext context, SocketChannel socketChannel, boolean clientMode) throws Exception {
        // 原始的NIO SOCKET
        this.socketChannel = socketChannel;

        // 初始化BUFFER
        SSLSession dummySession = context.createSSLEngine().getSession();
        myAppData = ByteBuffer.allocate(dummySession.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(dummySession.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(dummySession.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(dummySession.getPacketBufferSize());
        dummySession.invalidate();

        engine = context.createSSLEngine();
        engine.setUseClientMode(clientMode);
        engine.beginHandshake();
    }

    /**
     * 参考 https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html
     * 实现的 SSL 的握手协议
     * @return
     * @throws IOException
     */
    protected boolean doHandshake() throws IOException {
        SSLEngineResult result;
        SSLEngineResult.HandshakeStatus handshakeStatus;

        int appBufferSize = engine.getSession().getApplicationBufferSize();
        ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        myNetData.clear();
        peerNetData.clear();

        handshakeStatus = engine.getHandshakeStatus();
        while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    if (socketChannel.read(peerNetData) < 0) {
                        if (engine.isInboundDone() && engine.isOutboundDone()) {
                            return false;
                        }
                        try {
                            engine.closeInbound();
                        } catch (SSLException e) {
                            log.debug("收到END OF STREAM，关闭连接.", e);
                        }
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    peerNetData.flip();
                    try {
                        result = engine.unwrap(peerNetData, peerAppData);
                        peerNetData.compact();
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException sslException) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK:
                            break;
                        case BUFFER_OVERFLOW:
                            //peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                            break;
                        case BUFFER_UNDERFLOW:
                            //peerNetData = handleBufferUnderflow(engine, peerNetData);
                            break;
                        case CLOSED:
                            if (engine.isOutboundDone()) {
                                return false;
                            } else {
                                engine.closeOutbound();
                                handshakeStatus = engine.getHandshakeStatus();
                                break;
                            }
                        default:
                            throw new IllegalStateException("无效的握手状态: " + result.getStatus());
                    }
                    break;
                case NEED_WRAP:
                    myNetData.clear();
                    try {
                        result = engine.wrap(myAppData, myNetData);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException sslException) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK :
                            myNetData.flip();
                            while (myNetData.hasRemaining()) {
                                socketChannel.write(myNetData);
                            }
                            break;
                        case BUFFER_OVERFLOW:
                            //myNetData = enlargePacketBuffer(engine, myNetData);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("加密后消息内容为空，报错");
                        case CLOSED:
                            try {
                                myNetData.flip();
                                while (myNetData.hasRemaining()) {
                                    socketChannel.write(myNetData);
                                }
                                peerNetData.clear();
                            } catch (Exception e) {
                                handshakeStatus = engine.getHandshakeStatus();
                            }
                            break;
                        default:
                            throw new IllegalStateException("无效的握手状态: " + result.getStatus());
                    }
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        executor.execute(task);
                    }
                    handshakeStatus = engine.getHandshakeStatus();
                    break;
                case FINISHED:
                    break;
                case NOT_HANDSHAKING:
                    break;
                default:
                    throw new IllegalStateException("无效的握手状态: " + handshakeStatus);
            }
        }

        return true;
    }

    /**
     * 参考 https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html
     * 实现的 SSL 的传输读取协议
     * @param consumer
     * @throws IOException
     */
    public void read(Consumer<ByteBuffer> consumer) throws IOException {
        // BUFFER初始化
        peerNetData.clear();
        int bytesRead = socketChannel.read(peerNetData);
        if (bytesRead > 0) {
            peerNetData.flip();
            while (peerNetData.hasRemaining()) {
                peerAppData.clear();
                SSLEngineResult result = engine.unwrap(peerNetData, peerAppData);
                switch (result.getStatus()) {
                    case OK:
                        log.debug("收到远程的返回结果消息为：" + new String(peerAppData.array(), 0, peerAppData.position()));
                        consumer.accept(peerAppData);
                        peerAppData.flip();
                        break;
                    case BUFFER_OVERFLOW:
                        //peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                        break;
                    case BUFFER_UNDERFLOW:
                        //peerNetData = handleBufferUnderflow(engine, peerNetData);
                        break;
                    case CLOSED:
                        log.debug("收到远程连接关闭消息.");
                        closeConnection();
                        return;
                    default:
                        throw new IllegalStateException("无效的握手状态: " + result.getStatus());
                }
            }
        } else if (bytesRead < 0) {
            log.debug("收到END OF STREAM，关闭连接.");
            handleEndOfStream();
        }
    }

    public void write(String message) throws IOException {
        write(ByteBuffer.wrap(message.getBytes()));
    }

    /**
     * 参考 https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html
     * 实现的 SSL 的传输写入协议
     * @param message
     * @throws IOException
     */
    public void write(ByteBuffer message) throws IOException {
        myAppData.clear();
        myAppData.put(message);
        myAppData.flip();
        while (myAppData.hasRemaining()) {
            myNetData.clear();
            SSLEngineResult result = engine.wrap(myAppData, myNetData);
            switch (result.getStatus()) {
                case OK:
                    myNetData.flip();
                    while (myNetData.hasRemaining()) {
                        socketChannel.write(myNetData);
                    }
                    log.debug("写入远程的消息为: {}", message);
                    break;
                case BUFFER_OVERFLOW:
                    //myNetData = enlargePacketBuffer(engine, myNetData);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new SSLException("加密后消息内容为空.");
                case CLOSED:
                    closeConnection();
                    return;
                default:
                    throw new IllegalStateException("无效的握手状态: " + result.getStatus());
            }
        }
    }

    /**
     * 关闭连接
     * @throws IOException
     */
    public void closeConnection() throws IOException  {
        engine.closeOutbound();
        doHandshake();
        socketChannel.close();
        executor.shutdown();
    }

    /**
     * END OF STREAM(-1)默认是关闭连接
     * @throws IOException
     */
    protected void handleEndOfStream() throws IOException  {
        try {
            engine.closeInbound();
        } catch (Exception e) {
            log.error("END OF STREAM 关闭失败.", e);
        }
        closeConnection();
    }

}