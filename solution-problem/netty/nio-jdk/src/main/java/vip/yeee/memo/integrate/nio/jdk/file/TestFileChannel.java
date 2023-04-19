package vip.yeee.memo.integrate.nio.jdk.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/11/25 10:05
 */
public class TestFileChannel {


    public static void main(String[] args) throws IOException {

        Stream.of(new ArrayList<>(), new ArrayList<>()).flatMap(Collection::stream).forEach(System.out::println);

    }

    /**
     * 通道之间通过transfer()实现数据的传输(直接操作缓冲区)：
     */
    public static void testUseTransferCopyFile() throws IOException {

        try (FileChannel oriFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);
             FileChannel newFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证COPY.jpg")
                             , StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);) {
            oriFileChannel.transferTo(0, oriFileChannel.size(), newFileChannel);
        }
    }

    /**
     * 使用内存映射文件的方式实现文件复制的功能(直接操作缓冲区)：
     */
    public static void testUseMappedByteBufferCopyFile() throws IOException {

        try (FileChannel oriFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);
             FileChannel newFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证COPY.jpg")
                , StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);) {
            // 内存映射文件
            MappedByteBuffer oriMb = oriFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, oriFileChannel.size());
            MappedByteBuffer newMb = newFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, oriFileChannel.size());

            // 直接对缓冲区进行数据的读写
            byte[] bytes = new byte[oriMb.limit()];
            oriMb.get(bytes);
            newMb.put(bytes);
        }

    }

    /**
     *  使用FileChannel配合缓冲区实现文件复制的功能：
     */
    public static void testUseFileChannelCopyFile() throws IOException {

        try (FileChannel oriFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证.jpg"), StandardOpenOption.READ);
             FileChannel newFileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\temp\\XXX_毕业证COPY.jpg")
                             , StandardOpenOption.WRITE, StandardOpenOption.CREATE);) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (oriFileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                newFileChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        }

    }

    public static void testGetFileChannel() throws IOException {
        // 1.
        FileInputStream inputStream = new FileInputStream("///");
        inputStream.getChannel();

        // 2.
        FileChannel.open(Paths.get(""), StandardOpenOption.READ);
    }

    public static void testCreateByteBuffer() {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 看一下初始时4个核心变量的值
        System.out.println("初始时-->limit--->"+byteBuffer.limit());
        System.out.println("初始时-->position--->"+byteBuffer.position());
        System.out.println("初始时-->capacity--->"+byteBuffer.capacity());
        System.out.println("初始时-->mark--->" + byteBuffer.mark());

        System.out.println("--------------------------------------");

        // 添加一些数据到缓冲区中
        String s = "Java3y";
        byteBuffer.put(s.getBytes());

        // 看一下初始时4个核心变量的值
        System.out.println("put完之后-->limit--->"+byteBuffer.limit());
        System.out.println("put完之后-->position--->"+byteBuffer.position());
        System.out.println("put完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("put完之后-->mark--->" + byteBuffer.mark());



        System.out.println("--------------------------------------");

        byteBuffer.flip();
        System.out.println("flip完之后-->limit--->"+byteBuffer.limit());
        System.out.println("flip完之后-->position--->"+byteBuffer.position());
        System.out.println("flip完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("flip完之后-->mark--->" + byteBuffer.mark());



        System.out.println("--------------------------------------");

        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes, Charset.defaultCharset()));

        System.out.println("--------------------------------------");

        System.out.println("get完之后-->limit--->"+byteBuffer.limit());
        System.out.println("get完之后-->position--->"+byteBuffer.position());
        System.out.println("get完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("get完之后-->mark--->" + byteBuffer.mark());



        System.out.println("--------------------------------------");

        byteBuffer.clear();
        System.out.println("clear完之后-->limit--->"+byteBuffer.limit());
        System.out.println("clear完之后-->position--->"+byteBuffer.position());
        System.out.println("clear完之后-->capacity--->"+byteBuffer.capacity());
        System.out.println("clear完之后-->mark--->" + byteBuffer.mark());
    }

}

