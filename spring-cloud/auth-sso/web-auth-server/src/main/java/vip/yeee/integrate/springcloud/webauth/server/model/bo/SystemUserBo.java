package vip.yeee.integrate.springcloud.webauth.server.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.AuthUser;

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
