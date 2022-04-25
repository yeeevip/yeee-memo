package com.impl;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/24 10:15
 */
public class SimpleTransProxy {


    /*
     * 测试

        代理的测试比较简单，指向代码后，代理服务监听8006端口，此时：

        curl -x "localhost:8006" http://httpbin.org/get 测试HTTP请求

        curl -x "localhost:8006" https://httpbin.org/get 测试HTTPS请求
    **/

    public static void main(String[] args) throws IOException {
        int port = 8006;
        ServerSocketChannel localServer = ServerSocketChannel.open();
        localServer.bind(new InetSocketAddress(port));
        Reactor reactor = new Reactor();
        // REACTOR线程
        GlobalThreadPool.REACTOR_EXECUTOR.submit(reactor::run);

        // WORKER单线程调试
        while (localServer.isOpen()) {
            // 此处阻塞等待连接
            SocketChannel remoteClient = localServer.accept();

            // 工作线程
            GlobalThreadPool.WORK_EXECUTOR.submit(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    // 代理到远程
                    SocketChannel remoteServer = new ProxyHandler().proxy(remoteClient);

                    // 透明传输
                    reactor.pipe(remoteClient, remoteServer)
                            .pipe(remoteServer, remoteClient);
                }
            });
        }
    }
}
