package vip.yeee.memo.demo.mq.rocketmq.consumer.v1.model;

/**
 * 消息类型
 */
public enum MessageType {

    /**
     * 普通消息
     */
    NORMAL,

    /**
     * 批量消息
     */
    BATCH,

    /**
     * 顺序消息
     */
    ORDER;

}
