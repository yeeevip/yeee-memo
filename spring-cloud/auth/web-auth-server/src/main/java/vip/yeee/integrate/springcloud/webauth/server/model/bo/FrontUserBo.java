package vip.yeee.integrate.springcloud.webauth.server.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.yeee.memo.integrate.base.websecurity.model.AuthUser;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FrontUserBo extends AuthUser {
}