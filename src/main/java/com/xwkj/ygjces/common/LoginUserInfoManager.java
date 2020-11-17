package com.xwkj.ygjces.common;

import com.xwkj.ygjces.model.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;

/**
 * 获取当前登录的用户信息
 */
public class LoginUserInfoManager {
    private static Log log = LogFactory.getLog(LoginUserInfoManager.class);

    /**
     * 获取当前登录的用户信息
     * @return
     */
    public static UserInfo getUserInfo(){
        Object object = SecurityUtils.getSubject().getSession().getAttribute("UserInfoLogin");
        log.info(object);
        return (UserInfo)object;
    }
}
