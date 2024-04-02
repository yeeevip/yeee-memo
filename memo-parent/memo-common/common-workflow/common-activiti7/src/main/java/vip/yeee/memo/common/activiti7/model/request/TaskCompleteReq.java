package vip.yeee.memo.common.activiti7.model.request;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2024/1/27 17:01
 */
@Data
public class TaskCompleteReq {

    private String taskId;

    private Integer isPass;

    private String remark;
}
