package vip.yeee.memo.demo.springcloud.webauth.server.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.yeee.memo.base.websecurityoauth2.model.AuthUser;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 17:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FrontUserBo extends AuthUser {
}
