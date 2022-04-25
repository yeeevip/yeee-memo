package com.yeee.zookeeper.lock.simple;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/29 18:24
 */
public interface Lock {

    boolean lock() throws Exception;

    boolean unlock();

}
