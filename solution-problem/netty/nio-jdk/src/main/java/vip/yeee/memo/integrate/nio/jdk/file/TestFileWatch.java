package vip.yeee.memo.integrate.nio.jdk.file;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * 文件监听
 *
 * @author yeeee
 * @since 2022/7/13 9:31
 */
public class TestFileWatch {

    static class TestNioWatchService {

        public static void main(String[] args) throws IOException {
            // 这里的监听必须是目录
            Path path = Paths.get("C:/Users/yeeee/Desktop/temp/download/testwatch");
            // 创建WatchService，它是对操作系统的文件监视器的封装，相对之前，不需要遍历文件目录，效率要高很多
            WatchService watcher = FileSystems.getDefault().newWatchService();
            // 注册指定目录使用的监听器，监视目录下文件的变化；
            // PS：Path必须是目录，不能是文件；
            // StandardWatchEventKinds.ENTRY_MODIFY，表示监视文件的修改事件
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            // 创建一个线程，等待目录下的文件发生变化
            try {
                while (true) {
                    // 获取目录的变化:
                    // take()是一个阻塞方法，会等待监视器发出的信号才返回。
                    // 还可以使用watcher.poll()方法，非阻塞方法，会立即返回当时监视器中是否有信号。
                    // 返回结果WatchKey，是一个单例对象，与前面的register方法返回的实例是同一个；
                    WatchKey key = watcher.take();
                    // 处理文件变化事件：
                    // key.pollEvents()用于获取文件变化事件，只能获取一次，不能重复获取，类似队列的形式。
                    for (WatchEvent<?> event : key.pollEvents()) {
                        // event.kind()：事件类型
                        if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                            //事件可能lost or discarded
                            continue;
                        }
                        // 返回触发事件的文件或目录的路径（相对路径）
                        Path fileName = (Path) event.context();
                        System.out.println("文件更新: " + fileName);
                    }
                    // 每次调用WatchService的take()或poll()方法时需要通过本方法重置
                    if (!key.reset()) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static class TestApacheCommonsIOWatchFile {

        public static void main(String[] args) throws Exception {
            FileMonitor fileMonitor = new FileMonitor(1000);
            fileMonitor.monitor("C:/Users/yeeee/Desktop/temp/download/testwatch/", new FileListener());
            fileMonitor.start();
        }

    }

}

class FileListener extends FileAlterationListenerAdaptor {

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
        System.out.println("onStart");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("新建：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("修改：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("删除：" + directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("新建：" + compressedPath);
        if (file.canRead()) {
            // TODO 读取或重新加载文件内容
            System.out.println("文件变更，进行处理");
        }
    }

    @Override
    public void onFileChange(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("修改：" + compressedPath);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("删除：" + file.getAbsolutePath());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
        System.out.println("onStop");
    }

}

class FileMonitor {
    private FileAlterationMonitor monitor;

    public FileMonitor(long interval) {
        monitor = new FileAlterationMonitor(interval);
    }
    /**
     * 给文件添加监听
     *
     * @param path 文件路径
     * @param listener 文件监听器
     */
    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    public void start() throws Exception {
        monitor.start();

    }
}