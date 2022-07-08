package vip.yeee.memo.integrate.simpletool.execlinux;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/27 14:49
 */
@Getter
@Setter
public abstract class AbstractJobDefinition {

    protected String jobGroup;
    protected String jobDescription;
    protected String triggerGroup;
    protected String triggerDescription;
    protected Instant triggerStartAt;
    protected String triggerCronExpression;

}
