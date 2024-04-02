package vip.yeee.memo.demo.distribute.lock.zookeeper.lock.simple;

/**
 * description ...
 *
 * @author https://www.yeee.vipe
 * @since 2021/12/29 18:24
 */
public interface Lock {

    boolean lock() throws Exception;

    boolean unlock();

}
