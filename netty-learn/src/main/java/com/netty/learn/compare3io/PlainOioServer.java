package com.netty.learn.compare3io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 阻塞IO
 * @Author: anchun
 * @Date: 2021/12/14 15:14
 */
public class PlainOioServer {

    public static void main(String[] args) throws IOException {
        PlainOioServer plainOioServer = new PlainOioServer();
        plainOioServer.serve(8080);
    }

    public void serve(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port); // 1
        try {
            for (;;) {
                final Socket clientSocket = serverSocket.accept(); // 2
                System.out.println("Accepted connection from " + clientSocket);

                new Thread(() -> {                                 // 3
                    OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        out.write("Hi！\r\n".getBytes(StandardCharsets.UTF_8));  // 4
                        out.flush();
                        clientSocket.close();                       // 5
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            // ignore on close
                        }
                    }
                }).start();                                             // 6
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
