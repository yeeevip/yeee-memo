package vip.yeee.memo.integrate.nio.jdk.chartapp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/6 18:17
 */
public class ChatServer implements Runnable {

    private Selector selector;
    private SelectionKey serverKey;
    private Vector<String> usernames;
    private static final int PORT = 9999;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ChatServer() {
        usernames = new Vector<>();
        init();
    }

    private void init() {

        try {
            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));

            serverSocketChannel.configureBlocking(false);

            serverKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            printInfo("server starting..............");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        try {
            while (selector.select() > 0) {

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();

                    if (selectionKey.isAcceptable()) {

                        System.out.println(selectionKey.toString() + " : 接收");

                        //一定要把这个accpet状态的服务器key去掉，否则会出错
                        keyIterator.remove();

                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) serverKey.channel();

                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ);

                    }

                    //若此key的通道是有数据可读状态
                    if (selectionKey.isValid() && selectionKey.isReadable()) {

                        System.out.println(selectionKey.toString() + " : 读");
                        readMsg(selectionKey);

                    }

                    //若此key的通道是写数据状态
                    if (selectionKey.isValid() && selectionKey.isWritable()) {
                        System.out.println(selectionKey.toString() + " : 写");
                        writeMsg(selectionKey);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMsg(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            Object attachment = selectionKey.attachment();

            //获取key的值之后，要把key的值置空，避免影响下一次的使用
            selectionKey.attach("");
            socketChannel.write(ByteBuffer.wrap(attachment.toString().getBytes(StandardCharsets.UTF_8)));
            selectionKey.interestOps(SelectionKey.OP_READ);

        } catch (Exception e) {
            System.out.println("readMsg err");
            e.printStackTrace();
            serverKey.cancel();
            try {
                if (socketChannel != null) {
                    socketChannel.socket().close();
                    socketChannel.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void readMsg(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //假如客户端关闭了通道，这里在对该通道read数据，会发生IOException，捕获到Exception后，关闭掉该channel，取消掉该key
            int len = 0;

            StringBuilder sb = new StringBuilder();

            // 如果读到了数据
            while ((len = socketChannel.read(buffer))> 0) {
                // 让buffer翻转， 把buffer中的数据读出来
                buffer.flip();
                sb.append(new String(buffer.array(), 0, len));
            }

            String msg = sb.toString();

            if (msg.contains("open_")) {

                String name = msg.substring(5);//取出名字
                printInfo(name + " --> online");

                usernames.add(name);

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey skey = keyIterator.next();
                    //若不是服务器套接字通道的key，则将数据设置到此key中
                    //并更新此key感兴趣的动作
                    if(skey != serverKey){
                        skey.attach(usernames);
                        skey.interestOps(skey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }

            } else if (msg.contains("exit_")) {// 如果是下线时发送的数据

                String username = msg.substring(5);
                usernames.remove(username);
                selectionKey.attach("close");


                //要退出的当前channel加上close的标示，并把兴趣转为写，如果write中收到了close，则中断channel的链接
                selectionKey.interestOps(SelectionKey.OP_WRITE);


                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey sKey = iter.next();
                    if(sKey != serverKey) {
                        sKey.attach(usernames);
                        sKey.interestOps(sKey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }

            } else {// 如果是聊天发送数据

                String uname = msg.substring(0, msg.indexOf("^"));
                msg = msg.substring(msg.indexOf("^") + 1);
                printInfo("("+uname+")说：" + msg);
                String dateTime = sdf.format(new Date());
                String smsg = uname + " " + dateTime + "\n  " + msg + "\n";


                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while(iter.hasNext()){
                    SelectionKey sKey = iter.next();
                    if (sKey != serverKey) {
                        sKey.attach(smsg);
                        sKey.interestOps(sKey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }

            }

            buffer.clear();

        } catch (IOException e) {

            System.out.println("readMsg err");
            e.printStackTrace();

            //当客户端关闭channel时，服务端再往通道缓冲区中写或读数据，都会报IOException，解决方法是：在服务端这里捕获掉这个异常，并且关闭掉服务端这边的Channel通道
            selectionKey.cancel();
            try {
                socketChannel.socket().close();
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void printInfo(String str) {
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        new Thread(server).start();
    }


}
