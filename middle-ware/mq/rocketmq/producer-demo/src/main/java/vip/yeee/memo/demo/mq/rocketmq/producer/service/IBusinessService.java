package vip.yeee.memo.demo.mq.rocketmq.producer.service;

import com.aliyun.openservices.ons.api.Message;

public interface IBusinessService {
    boolean executeService(final Message msg);
    boolean checkService(final Message msg);
}
