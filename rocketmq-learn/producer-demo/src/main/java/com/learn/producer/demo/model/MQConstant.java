package com.learn.producer.demo.model;

/**
 * 消息队列常量
 */
public class MQConstant {

    private MQConstant() {}

    // ********************************************* TOPIC *********************************************

    /**
     * 普通消息topic
     */
    public static final String NORMAL_TOPIC = "common_topic";


    // ********************************************* CONSUMER *********************************************

    /**
     * 默认消费组
     */
    public static final String DEFAULT_GROUP = "default_group";


    // ********************************************* TAG *********************************************

    public static final String DEMO_MESSAGE_TAG = "demo_message_tag";
}
