package com.impl.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: anchun
 * @Date: 2021/11/29 9:48
 */
public class TestPipe {

    public static void main(String[] args) throws IOException {

        // 1.获得管道
        Pipe pipe = Pipe.open();

        // 2.将缓冲区的数据写到管道
        ByteBuffer buf = ByteBuffer.allocate(1024);

        Pipe.SinkChannel sinkChannel = pipe.sink();
        buf.put("通过单向管道发送数据".getBytes(StandardCharsets.UTF_8));
        buf.flip();
        sinkChannel.write(buf);
        buf.clear();

        // 3.读取缓冲区的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        int len = sourceChannel.read(buf);
        System.out.println(new String(buf.array(), 0, len));

        sinkChannel.close();
        sourceChannel.close();


    }

}
