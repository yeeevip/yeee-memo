package vip.yeee.memo.integrate.mq.rocketmq.consumer.v1.model;

/**
 * 消息模式
 */
public enum ConsumeMode {

    /**
     * 集群消费模式
     */
    CLUSTERING,

    /**
     * 广播消费模式
     */
    BROADCASTING;
}
