package vip.yeee.memo.common.activiti7.model.request;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/18 17:28
 */
@Data
public class InstCreateReq {

    private String pdId;

    private String instanceName;

    private String instanceDesc;
}
