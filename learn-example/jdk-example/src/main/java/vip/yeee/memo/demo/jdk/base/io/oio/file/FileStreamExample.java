package vip.yeee.memo.demo.jdk.base.io.oio.file;

import cn.hutool.core.io.IoUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/21 16:35
 */
public class FileStreamExample {

    public static void main(String[] args) {

    }

    public String readFile1() throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream("D:\\desktop\\in.txt"))) {
            return IoUtil.readUtf8(in);
        }
    }

    public void writeFile1() throws IOException {
        String txt = readFile1();
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\desktop\\out.txt"));
             InputStream in = new BufferedInputStream(new ByteArrayInputStream(txt.getBytes(StandardCharsets.UTF_8)))) {
            IoUtil.copy(in, out);
        }
    }

}
