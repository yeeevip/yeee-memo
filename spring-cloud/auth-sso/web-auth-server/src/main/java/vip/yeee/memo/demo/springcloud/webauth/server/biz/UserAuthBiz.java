package vip.yeee.memo.demo.springcloud.webauth.server.biz;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.memo.demo.springcloud.webauth.server.service.UserAuthService;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.common.domain.entity.sys.SysUser;
import vip.yeee.memo.common.domain.mapper.sys.SysUserMapper;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 13:18
 */
@Slf4j
@Component
public class UserAuthBiz {

    @Resource
    private UserAuthService userAuthService;
    @Resource
    private SysUserMapper sysUserMapper;

    public Void systemUserRegister(UserAuthRequest request) {
        if (!StrUtil.isAllNotBlank(request.getUsername(), request.getPassword())) {
            throw new BizException("用户名或者密码不能为空");
        }
        SysUser save = new SysUser();
        save.setUsername(request.getUsername());
        save.setPassword(userAuthService.decodePassword(request.getPassword()));
        sysUserMapper.insert(save);
        return null;
    }

}
