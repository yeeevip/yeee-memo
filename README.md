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
___

4.性能、[集群高可用下]安全问题及存在的问题分析

+ 集群高可用下安全问题：客户端1加锁后master宕机并未同步到新的master，客户端2会加锁成功
    1. 引入Redlock，大部分节点(N/2+1)加锁成功，但是性能上有损耗 >问题> 节点奔溃重启、时间跳跃问题、计算获取锁的过程超时

+ 性能：

  1. 高并发下加锁性能很高
  2. 基于事件selector的操作

+ 问题：
  1. GC时STW导致未能续期导致锁释放问题

## 基于zookeeper的分布式锁

> 所属模块 distribute-lock-zookeeper

1.lock

+ com.yeee.zookeeper.lock.simple.ZkLock

2.com.101tec.zkclient api demo

+ com.yeee.zookeeper.zkClientApi.ZkClientApiDemo

3.使用轮子 org.apache.curator.framework.recipes.locks.InterProcessMutex

+ com.yeee.zookeeper.lock.curator.InterProcessMutexLock

---

4.性能、[集群高可用下]安全问题及存在的问题分析

+ 集群高可用下安全问题：投票确认机制及同步写操作到follower节点，保证数据的一致性

+ 性能：
  
  1. 高并发下加锁性能相对低效，与其本身【高可用】特点及节点的增删是在操作文件系统有很大关系
  2. 【监听等待通知机制】，防止【羊群效应】，避免客户端无故轮询争夺锁造成的性能损耗
  3. 串行化操作
    
+ 问题：
    1. GC时STW导致临时节点释放问题

总之，采用Zookeeper作为分布式锁，你要么就获取不到锁，一旦获取到了，必定节点的数据是一致的，不会出现redis那种异步同步导致数据丢失的问题。

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

> _com.yeee.threadPool.CompletableFutureTest_
> + 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待
> 
> + Jdk1.8新引入CompletableFuture，它针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法
> 
> + thenAccept()、exceptionally()、thenApplyAsync()、anyOf()、allOf() 函数的基本用法，详见com.yeee.threadPool.CompletableFutureTest

2.ThreadPool 

> _com.yeee.threadPool.ThreadPoolTest_
> + Java线程的创建、销毁、和线程切换比较耗费计算机资源
> + 1>降低系统资源消耗 >2提高系统响应速度 >3提高线程的可管理性 >4更强大的功能，延时定时线程池

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