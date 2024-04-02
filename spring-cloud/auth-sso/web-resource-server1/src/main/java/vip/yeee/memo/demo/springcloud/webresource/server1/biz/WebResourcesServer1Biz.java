package vip.yeee.memo.demo.springcloud.webresource.server1.biz;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.websecurityoauth2.context.SecurityContext;
import vip.yeee.memo.base.websecurityoauth2.model.AuthedUser;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/17 13:18
 */
@Slf4j
@Component
public class WebResourcesServer1Biz {

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
