package vip.yeee.memo.demo.jdk.base.io.nio.aio.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/23 13:14
 */
public class FileAsyncChannelExample {

    public static void main(String[] args) throws IOException {
        readTextFile();
    }


    public static void readTextFile() throws IOException {
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("C:\\Users\\yeeee\\Desktop\\新建 文本文档 (2).txt"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        final long[] position = {0};
        fileChannel.read(buffer, position[0], buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (result != -1) {
                    attachment.flip();
                    byte[] bytes = new byte[attachment.limit()];
                    attachment.get(bytes);
                    System.out.println(new String(bytes));
                    attachment.clear();
                    position[0] += result;  // 更新读取位置
                    fileChannel.read(buffer, position[0], buffer, this);  // 继续读取
                } else {
                    try {
                        fileChannel.close();  // 关闭文件通道
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Done reading file");
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    fileChannel.close();  // 关闭文件通道
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Error reading file: " + exc.getMessage());
            }
        });

        // 防止主线程退出
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}