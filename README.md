## 基于jedis的redis分布式锁

> 所属模块 distribute-lock-jedis

## 基于redisson的redis分布式锁

> 所属模块 distribute-lock-redisson

### 分布式锁RLock

1.初始化RLock

+ RLock lock = redissonClient.getLock("redisson:test:lock；1")

___

2.获得锁

+ lock.lock() // 不指定过期时间，通过watchdog实现自动续期
 
+ lock.tryLock(long waitTime, long leaseTime, TimeUnit unit) // 指定释放时间，到期释放不续期

___

3.释放锁

+ lock.unlock()


## 基于zookeeper的分布式锁

> 所属模块 distribute-lock-zookeeper

1.lock

+ com.yeee.zookeeper.lock.simple.ZkLock

2.com.101tec.zkclient api demo

+ com.yeee.zookeeper.zkClientApi.ZkClientApiDemo

3.使用轮子 org.apache.curator.framework.recipes.locks.InterProcessMutex

+ com.yeee.zookeeper.lock.curator.InterProcessMutexLock

## JDK基础

> 所属模块 jdk-base-learn

### java基本类型

```
    byte b = 1;                             // byte b = 0;     一个字节 8位
    byte[] shortByte = new byte[2];         // short s = 0;    二个字节 16位
    byte[] intByte = new byte[4];           // int i = 0;      四个字节 32位
    byte[] longByte = new byte[8];          // long l = 0l;    八个字节 64位
    byte[] floatByte = new byte[4];         // float f = 0.0f; 四个字节 32位
    byte[] doubleByte = new byte[8];        // double d = 0.0d;八个字节 64位
    byte[] charByte = new byte[2];          // char c = '';    二个字节 16位
    byte[] booleanByte = new byte[1];       // boolean b = false;  一个字节 8位
```

### 线程池

1.CompletableFuture

> + 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待
> 
> + Jdk1.8新引入CompletableFuture，它针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法
> 
> + thenAccept()、exceptionally()、thenApplyAsync()、anyOf()、allOf() 函数的基本用法，详见com.yeee.threadPool.CompletableFutureTest

## 基于Netty的SimpleHttpWebServer

> 所属模块 nio-learn-ImplWebServerByNetty

## JDK自带的NIO

> 所属模块 nio-learn-jdk

## Netty基本使用

> 所属模块 nio-learn-netty

## 算法学习PAT-Coding

> 所属模块 pat-coding

## spring-cloud相关

> 所属模块 spring-cloud-learn

### 响应式reactive编程

> 所属模块 learn1-reactive-programming

### openfeign

> 服务端 learn2-feign-service-server / 客户端 learn2-feign-service-client

___

1. openfeign.FallbackFactory中处理异常，防止普通的业务异常促使客户端服务降级

> com.yeee.feign.BookService.FallbackImpl

___

2. 自定义Decoder解码器，可以将响应压缩gzip、解析服务端封装的Result中的data实体

> com.yeee.feign.FeignConfiguration.feignDecoder/decoder

> com.yeee.feign.FeignResultDecoder

___

## 自己的简单工具类

> 所属模块 simple-tools-my

## gatling测试

> 所属模块 test-tool-gatling-scripts

## webservice-demo

> 所属模块 webservice-demo

## webservice-demo

> 所属模块 websocket-spring