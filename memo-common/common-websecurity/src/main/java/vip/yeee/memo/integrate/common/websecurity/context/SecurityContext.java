package vip.yeee.memo.integrate.common.websecurity.context;

import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.integrate.common.websecurity.model.AuthedUser;

@Slf4j
public class SecurityContext {

    public static ThreadLocal<AuthedUser> curUser = new ThreadLocal<>();
    public static ThreadLocal<String> curToken = new ThreadLocal<>();

    /**
     * 返回当前用户ID
     */
    public static String getCurUserId() {
        AuthedUser authedUser = curUser.get();
        if (authedUser != null) {
            return authedUser.getId();
        }
        return null;
    }

    public static AuthedUser getCurUser() {
        return curUser.get();
    }

    public static String getCurToken() {
        return curToken.get();
    }

}
