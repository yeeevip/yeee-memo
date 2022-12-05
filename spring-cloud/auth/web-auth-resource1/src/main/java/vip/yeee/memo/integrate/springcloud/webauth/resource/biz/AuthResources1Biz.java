package vip.yeee.memo.integrate.springcloud.webauth.resource.biz;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.websecurity.context.SecurityContext;
import vip.yeee.memo.integrate.base.websecurity.model.AuthedUser;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 13:18
 */
@Component
public class AuthResources1Biz {

    public String accessApi() {
        AuthedUser curUser = SecurityContext.getCurUser();
        return StrUtil.format("id = {}, username = {}, 访问接口成功", curUser.getId(), curUser.getUsername());
    }


    public String anonymousApiByAnno() {
        return "访问【注解标记匿名】接口成功";
    }

    public String anonymousApiByLimit() {
        return "访问【配置限制匿名】接口成功";
    }

}
