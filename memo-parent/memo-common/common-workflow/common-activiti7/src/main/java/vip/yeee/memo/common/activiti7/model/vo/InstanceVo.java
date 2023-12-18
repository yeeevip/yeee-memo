package vip.yeee.memo.common.activiti7.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/15 17:45
 */
@Data
public class InstanceVo {

    private String id;

    private String name;

    private String status;

    private String processDefinitionId;

    private String processDefinitionKey;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    private Integer processDefinitionVersion;

    private String resourceName;

    private String deploymentId;
}
