package vip.yeee.memo.common.appauth.client.context;

import org.slf4j.Logger;
import vip.yeee.memo.base.util.LogUtils;
import vip.yeee.memo.common.appauth.client.model.ApiAuthedUser;

public class ApiSecurityContext {

    private final static Logger log = LogUtils.commonAuthLog();
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
