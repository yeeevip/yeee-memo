package vip.yeee.memo.demo.springcloud.webauth.server.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.yeee.memo.base.websecurityoauth2.model.AuthUser;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemUserBo extends AuthUser {
}
