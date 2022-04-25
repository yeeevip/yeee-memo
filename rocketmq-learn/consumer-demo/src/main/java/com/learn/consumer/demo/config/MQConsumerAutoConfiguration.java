package com.learn.consumer.demo.config;

import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.Admin;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.aliyun.openservices.ons.api.bean.BatchConsumerBean;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.learn.consumer.demo.annotation.RocketMQMessageListener;
import com.learn.consumer.demo.model.ConsumeMode;
import com.learn.consumer.demo.model.MessageType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * 消费自动配置类
 */
@Configuration
public class MQConsumerAutoConfiguration implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;
    private final List<Admin> consumerBeans = new ArrayList<>();
    private Map<String, String> validConsumerMap = new HashMap<>();

    @Autowired(required = false)
    private MqConfig mqConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @PostConstruct
    public void init() throws Exception {
        // 查询listener
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class);
        // 订阅关系
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishConsumer(entry.getKey(), entry.getValue());
        }

        // 清空map，等待回收
        validConsumerMap = null;
    }

    @PreDestroy
    public void destroy() throws Exception {
        for (Admin bean : consumerBeans) {
            bean.shutdown();
        }
    }

    /**
     * 发布消费
     *
     * @param beanName:
     * @param bean:
     * @return
     */
    private void publishConsumer(String beanName, Object bean) {
        RocketMQMessageListener listener = applicationContext.findAnnotationOnBean(beanName, RocketMQMessageListener.class);
        if (listener == null) {
            return;
        }
        // 检查consumerGroup
        if (StrUtil.isNotEmpty(validConsumerMap.get(listener.consumerGroup()))) {
            String exist = validConsumerMap.get(listener.consumerGroup());
            throw new RuntimeException("消费组重复订阅，请新增消费组用于新的topic和tag组合: " + listener.consumerGroup() + "已经订阅了" + exist);
        } else {
            validConsumerMap.put(listener.consumerGroup(), listener.topic() + "-" + listener.selectorExpression());
        }

        // 绑定订阅
        Admin consumer = null;
        if (MessageType.NORMAL.equals(listener.type())) { // 普通消息
            consumer = convertConsumer(mqConfig, listener.consumerGroup(), listener.consumeMode());
            ((ConsumerBean) consumer).setSubscriptionTable(convertListener((MessageListener) bean, listener.topic(), listener.selectorExpression()));
        } else if (MessageType.BATCH.equals(listener.type())) { // 批量消息
            consumer = convertBatchConsumer(mqConfig, listener.consumerGroup(), listener.consumeMode());
            ((BatchConsumerBean) consumer).setSubscriptionTable(convertBatchListener((BatchMessageListener) bean, listener.topic(), listener.selectorExpression()));
        } else if (MessageType.ORDER.equals(listener.type())) { // 顺序消息
            consumer = convertOrderConsumer(mqConfig, listener.consumerGroup());
            // 绑定订阅关系
            ((OrderConsumerBean) consumer).setSubscriptionTable(convertOrderListener((MessageOrderListener) bean, listener.topic(), listener.selectorExpression()));
        }

        // 启动
        Optional.ofNullable(consumer).orElseThrow(() -> new RuntimeException("RocketMQ init error")).start();
        consumerBeans.add(consumer);
    }

    /**
     * 基础配置
     *
     * @param mqConfig
     * @return
     */
    private ConsumerBean convertConsumer(MqConfig mqConfig, String groupId, ConsumeMode consumeMode) {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");

        // 广播订阅方式设置。
        if(ConsumeMode.BROADCASTING.equals(consumeMode)){
            properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        }

        consumerBean.setProperties(properties);
        return consumerBean;
    }

    /**
     * 批量配置
     *
     * @param mqConfig
     * @return
     */
    private BatchConsumerBean convertBatchConsumer(MqConfig mqConfig, String groupId, ConsumeMode consumeMode) {
        BatchConsumerBean consumerBean = new BatchConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");

        // 设置批量消费最大消息数量，当指定Topic的消息数量已经攒够128条，SDK立即执行回调进行消费。默认值：32，取值范围：1~1024。
        properties.setProperty(PropertyKeyConst.ConsumeMessageBatchMaxSize, String.valueOf(50));
        // 设置批量消费最大等待时长，当等待时间达到10秒，SDK立即执行回调进行消费。默认值：0，取值范围：0~450，单位：秒。
        properties.setProperty(PropertyKeyConst.BatchConsumeMaxAwaitDurationInSeconds, String.valueOf(10));

        // 广播订阅方式设置。
        if(ConsumeMode.BROADCASTING.equals(consumeMode)){
            properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        }

        consumerBean.setProperties(properties);
        return consumerBean;
    }

    /**
     * 顺序基础配置 注意：广播消费模式下不支持顺序消息。
     *
     * @return
     */
    private OrderConsumerBean convertOrderConsumer(MqConfig mqConfig, String groupId) {
        OrderConsumerBean orderConsumerBean = new OrderConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //将消费者线程数固定为20个 20为默认值
        orderConsumerBean.setProperties(properties);
        return orderConsumerBean;
    }

    /**
     * linstener
     *
     * @param topic
     * @param messageListener
     * @param tag
     * @return
     */
    private Map<Subscription, MessageListener> convertListener(MessageListener messageListener, String topic, String tag) {
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        // 订阅关系
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        subscriptionTable.put(subscription, messageListener);
        return subscriptionTable;
    }

    /**
     * linstener
     *
     * @param topic
     * @param messageListener
     * @param tag
     * @return
     */
    private Map<Subscription, MessageOrderListener> convertOrderListener(MessageOrderListener messageListener, String topic, String tag) {
        Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<>();
        // 订阅关系
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        subscriptionTable.put(subscription, messageListener);
        return subscriptionTable;
    }

    /**
     * linstener
     *
     * @param topic
     * @param messageListener
     * @param tag
     * @return
     */
    private Map<Subscription, BatchMessageListener> convertBatchListener(BatchMessageListener messageListener, String topic, String tag) {
        Map<Subscription, BatchMessageListener> subscriptionTable = new HashMap<>();
        // 订阅关系
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        subscriptionTable.put(subscription, messageListener);
        return subscriptionTable;
    }
}
