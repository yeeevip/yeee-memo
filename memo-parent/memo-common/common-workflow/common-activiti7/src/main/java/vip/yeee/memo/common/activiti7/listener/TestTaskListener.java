package vip.yeee.memo.common.activiti7.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/29 10:24
 */
@Slf4j
public class TestTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        String name = delegateTask.getName();
        String eventName = delegateTask.getEventName();
        String assignee = delegateTask.getAssignee();

        log.info("【任务监听】---名称 = {}，事件 = {}，被指定人 = {} ---", name, eventName, assignee);

        if (EVENTNAME_CREATE.equals(eventName)) {

        } else if (EVENTNAME_ASSIGNMENT.equals(eventName)) {

        } else if (EVENTNAME_COMPLETE.equals(eventName)) {

        } else if (EVENTNAME_DELETE.equals(eventName)) {

        }

    }
}
