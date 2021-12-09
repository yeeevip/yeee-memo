package com.impl.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/12/8 18:14
 */
public class TestExample1 {
    /**
     * @Description:
     *
     * 参考hbase RpcServer，编写了一个简洁版多Selector server，对nio怎么用，Selector如何选择事件会有更深入的认识。
     *
     * client端发送消息：内容长度 + 内容，200线程同时发送
     *
     * server端接收消息：解析内容长度和内容，返回2MB测试数据给客户端
     *
     *
     *
     *  Server端：一个accept selector，多个read selector，一个write selector
     */
}

/**
 * Created by wangkai8 on 17/1/5.
 */
class Server {

    public static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private BlockingQueue<Call> queue = new LinkedBlockingQueue<Call>();

    private Queue<Call> responseCalls = new ConcurrentLinkedQueue<Call>();

    volatile boolean running = true;

    private Responder responder = null;

    private static int NIO_BUFFER_LIMIT = 64 * 1024;

    private int handler = 10;


    class Listener extends Thread {

        Selector selector;
        Reader[] readers;
        int robin;
        int readNum;

        Listener(int port) throws IOException {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 150);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            readNum = 10;
            readers = new Reader[readNum];
            for(int i = 0; i < readNum; i++) {
                readers[i] = new Reader(i);
                readers[i].start();
            }
        }


        public void run() {
            while(running) {
                try {
                    selector.select();
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid()) {
                            if(key.isAcceptable()) {
                                doAccept(key);
                            }
                        }
                    }
                } catch (IOException e) {
                    LOG.error("", e);
                }
            }
        }

        public void doAccept(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel;
            while((socketChannel = serverSocketChannel.accept()) != null) {
                try {
                    socketChannel.configureBlocking(false);
                    socketChannel.socket().setTcpNoDelay(true);
                    socketChannel.socket().setKeepAlive(true);
                } catch (IOException e) {
                    socketChannel.close();
                    throw e;
                }
                Reader reader = getReader();
                try {
                    reader.startAdd();
                    SelectionKey readKey = reader.registerChannel(socketChannel);
                    Connection c = new Connection(socketChannel);
                    readKey.attach(c);
                } finally {
                    reader.finishAdd();
                }
            }
        }

        public Reader getReader() {
            if(robin == Integer.MAX_VALUE) {
                robin = 0;
            }
            return readers[(robin ++) % readNum];
        }
    }


    class Reader extends Thread {

        Selector readSelector;
        boolean adding;

        Reader(int i) throws IOException {
            setName("Reader-" + i);
            this.readSelector = Selector.open();
            LOG.info("Starting Reader-" + i + "...");
        }

        @Override
        public void run() {
            while(running) {
                try {
                    readSelector.select();
                    while(adding) {
                        synchronized(this) {
                            this.wait(1000);
                        }
                    }

                    Iterator<SelectionKey> it = readSelector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid()) {
                            if(key.isReadable()) {
                                doRead(key);
                            }
                        }
                    }
                } catch (IOException e) {
                    LOG.error("", e);
                } catch (InterruptedException e) {
                    LOG.error("", e);
                }
            }
        }

        public void doRead(SelectionKey selectionKey) {
            Connection c = (Connection) selectionKey.attachment();
            if(c == null) {
                return;
            }

            int n;
            try {
                n = c.readAndProcess();
            } catch (IOException e) {
                LOG.error("", e);
                n = -1;
            } catch (Exception e) {
                LOG.error("", e);
                n = -1;
            }
            if(n == -1) {
                c.close();
            }
        }

        public SelectionKey registerChannel(SocketChannel channel) throws IOException {
            return channel.register(readSelector, SelectionKey.OP_READ);
        }

        public void startAdd() {
            adding = true;
            readSelector.wakeup();
        }

        public synchronized void finishAdd() {
            adding = false;
            this.notify();
        }
    }


    class Connection {
        private SocketChannel channel;
        private ByteBuffer dataBufferLength;
        private ByteBuffer dataBuffer;
        private boolean skipHeader;

        public Connection(SocketChannel channel) {
            this.channel = channel;
            this.dataBufferLength = ByteBuffer.allocate(4);
        }

        public int readAndProcess() throws IOException {
            int count;
            if(!skipHeader) {
                count = channelRead(channel, dataBufferLength);
                if (count < 0 || dataBufferLength.remaining() > 0) {
                    return count;
                }
            }

            skipHeader = true;

            if(dataBuffer == null) {
                dataBufferLength.flip();
                int dataLength = dataBufferLength.getInt();
                dataBuffer = ByteBuffer.allocate(dataLength);
            }

            count = channelRead(channel, dataBuffer);

            if(count >= 0 && dataBuffer.remaining() == 0) {
                process();
            }

            return count;
        }


        /**
         * process the dataBuffer
         */
        public void process() {
            dataBuffer.flip();
            byte[] data = dataBuffer.array();
            Call call = new Call(this, data, responder);
            try {
                queue.put(call);
            } catch (InterruptedException e) {
                LOG.error("", e);
            }

        }


        public void close() {
            if(channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                }
            }
        }
    }


    class Responder extends Thread {

        Selector writeSelector;

        public Responder() throws IOException {
            writeSelector = Selector.open();
        }

        public void run() {
            while(running) {
                try {
                    registerWriters();
                    int n = writeSelector.select(1000);
                    if(n == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> it = writeSelector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid() && key.isWritable()) {
                            doAsyncWrite(key);
                        }
                    }
                } catch (IOException e) {
                    LOG.error("", e);
                }
            }
        }


        public void registerWriters() throws IOException {
            Iterator<Call> it = responseCalls.iterator();
            while(it.hasNext()) {
                Call call = it.next();
                it.remove();
                SelectionKey key = call.conn.channel.keyFor(writeSelector);
                try {
                    if (key == null) {
                        try {
                            call.conn.channel.register(writeSelector, SelectionKey.OP_WRITE, call);
                        } catch (ClosedChannelException e) {
                            //the client went away
                            if (LOG.isTraceEnabled())
                                LOG.trace("the client went away", e);
                        }
                    } else {
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                } catch (CancelledKeyException e) {
                    if (LOG.isTraceEnabled())
                        LOG.trace("the client went away", e);
                }
            }
        }


        public void registerForWrite(Call call) throws IOException {
            responseCalls.add(call);
            writeSelector.wakeup();
        }

        private void doAsyncWrite(SelectionKey key) throws IOException {
            Call call = (Call) key.attachment();
            if(call.conn.channel != key.channel()) {
                throw new IOException("bad channel");
            }
            int numBytes = channelWrite(call.conn.channel, call.response);
            if(numBytes < 0 || call.response.remaining() == 0) {
                try {
                    key.interestOps(0);
                } catch (CancelledKeyException e) {
                    LOG.warn("Exception while changing ops : " + e);
                }
            }
        }

        private void doResponse(Call call) throws IOException {
            //if data not fully send, then register the channel for async writer
            if(!processResponse(call)) {
                registerForWrite(call);
            }
        }

        private boolean processResponse(Call call) throws IOException {
            boolean error = true;
            try {
                int numBytes = channelWrite(call.conn.channel, call.response);
                if (numBytes < 0) {
                    throw new IOException("error socket write");
                }
                error = false;
            } finally {
                if(error) {
                    call.conn.close();
                }
            }
            if(!call.response.hasRemaining()) {
                call.done = true;
                return true;
            }
            return false;
        }
    }

    class Handler extends Thread {

        public Handler(int i) {
            setName("handler-" + i);
            LOG.info("Starting Handler-" + i + "...");
        }

        public void run() {
            while(running) {
                try {
                    Call call = queue.take();
                    process(call);
                } catch (InterruptedException e) {
                    LOG.error("", e);
                } catch (IOException e) {
                    LOG.error("", e);
                }
            }
        }

        public void process(Call call) throws IOException {
            byte[] request = call.request;
            String message = new String(request);
            LOG.info("received message: " + message);

            //each channel write 2MB data for test
            int dataLength = 2 * 1024 * 1024;
            ByteBuffer buffer = ByteBuffer.allocate(4 + dataLength);

            buffer.putInt(dataLength);
            writeDataForTest(buffer);
            buffer.flip();

            call.response = buffer;
            responder.doResponse(call);
        }
    }

    public void writeDataForTest(ByteBuffer buffer) {
        int n = buffer.limit() - 4;
        for(int i = 0; i < n; i++) {
            buffer.put((byte)0);
        }
    }


    class Call {
        Connection conn;
        byte[] request;
        Responder responder;
        ByteBuffer response;
        boolean done;
        public Call(Connection conn, byte[] request, Responder responder) {
            this.conn = conn;
            this.request = request;
            this.responder = responder;
        }
    }


    public int channelRead(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.read(buffer) : channelIO(channel, null, buffer);
    }

    public int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channelIO(null, channel, buffer);
    }


    public int channelIO(ReadableByteChannel readCh, WritableByteChannel writeCh, ByteBuffer buffer) throws IOException {
        int initRemaining = buffer.remaining();
        int originalLimit = buffer.limit();

        int ret = 0;
        try {
            while (buffer.remaining() > 0) {
                int ioSize = Math.min(buffer.remaining(), NIO_BUFFER_LIMIT);
                buffer.limit(buffer.position() + ioSize);
                ret = readCh == null ? writeCh.write(buffer) : readCh.read(buffer);
                if (ret < ioSize) {
                    break;
                }
            }
        } finally {
            buffer.limit(originalLimit);
        }

        int byteRead = initRemaining - buffer.remaining();
        return byteRead > 0 ? byteRead : ret;
    }


    public void startHandler() {
        for(int i = 0; i < handler; i++) {
            new Handler(i).start();
        }
    }


    public void start() throws IOException {
        // 监听新的请求、注册基于selector读请求数据的线程
        new Listener(10000).start();
        // 注册基于selector响应数据的线程
        responder = new Responder();
        responder.start();
        // 从队列里处理需要响应的channel
        startHandler();
        LOG.info("server startup! ");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}

/**
 * Created by wangkai8 on 17/1/6.
 */
class Client {

    public static final Logger LOG = LoggerFactory.getLogger(Client.class);

    Socket socket;
    OutputStream out;
    InputStream in;

    public Client() throws IOException {
        socket = SocketFactory.getDefault().createSocket();
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        InetSocketAddress server = new InetSocketAddress("localhost", 10000);
        socket.connect(server, 10000);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }


    public void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);
        out.flush();
    }


    public static void main(String[] args) throws IOException {
        int n = 200;
        for(int i = 0; i < n; i++) {
            new Thread() {
                Client client = new Client();

                public void run() {
                    try {
                        client.send(getName() + "_xiaomiemie");

                        DataInputStream inputStream = new DataInputStream(client.in);
                        int dataLength = inputStream.readInt();
                        byte[] data = new byte[dataLength];
                        inputStream.readFully(data);
                        client.socket.close();
                        LOG.info("receive from server: dataLength=" + data.length);
                    } catch (IOException e) {
                        LOG.error("", e);
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                }
            }.start();
        }
    }

}