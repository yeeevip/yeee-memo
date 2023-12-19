package vip.yeee.memo.base.websecurityoauth2.context;

import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.base.websecurityoauth2.model.AuthedUser;

@Slf4j
public class SecurityContext {

    public static ThreadLocal<AuthedUser> curUser = new ThreadLocal<>();

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

    public static boolean isSuperAdmin() {
        AuthedUser authedUser = curUser.get();
        if (authedUser != null) {
            return authedUser.getSuperAdmin();
        }
        return false;
    }

}
