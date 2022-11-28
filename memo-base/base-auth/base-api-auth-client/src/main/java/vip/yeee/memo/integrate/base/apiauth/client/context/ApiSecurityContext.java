package vip.yeee.memo.integrate.base.apiauth.client.context;

import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.integrate.base.apiauth.client.model.ApiAuthedUser;

@Slf4j
public class ApiSecurityContext {

    public static ThreadLocal<ApiAuthedUser> curUser = new ThreadLocal<>();

    /**
     * 返回当前用户ID
     */
    public static String getCurUserId() {
        ApiAuthedUser authedUser = curUser.get();
        if (authedUser != null) {
            return authedUser.getId();
        }
        return null;
    }

    public static ApiAuthedUser getCurUser() {
        return curUser.get();
    }

}
