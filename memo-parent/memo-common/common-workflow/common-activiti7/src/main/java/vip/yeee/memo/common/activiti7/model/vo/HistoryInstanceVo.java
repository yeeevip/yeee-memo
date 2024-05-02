package vip.yeee.memo.common.activiti7.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/15 17:45
 */
@Data
public class HistoryInstanceVo {

    private String id;

    private String processDefinitionId;

    private String processDefinitionKey;

    private String definitionName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String duration;

    private String deploymentId;

    private String resourceName;

}
