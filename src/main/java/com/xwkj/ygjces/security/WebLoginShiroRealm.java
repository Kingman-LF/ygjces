package com.xwkj.ygjces.security;

import com.xwkj.ygjces.Mapper.UserInfoMapper;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.PermissionInfoService;
import com.xwkj.ygjces.service.RoleInfoService;
import com.xwkj.ygjces.service.UserInfoService;
import org.apache.catalina.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Resource
 * @author
 */
public class WebLoginShiroRealm extends AuthorizingRealm {
    private static Log log = LogFactory.getLog(WebLoginShiroRealm.class);
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RoleInfoService roleInfoService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();
        //查询用户角色信息
        List<RoleInfo> roleInfoList = userInfoService.findRolesByUserId(userInfo.getId());
        if(roleInfoList != null){
            for (RoleInfo role : roleInfoList) {
                authorizationInfo.addRole(role.getRoleName());
                List<Permission> permissionList = roleInfoService.findPermissionByRoleId(role.getId());
                if(permissionList != null){
                    for (Permission p : permissionList) {
                        authorizationInfo.addStringPermission(p.getPermssion());
                    }
                }
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        SimpleAuthenticationInfo authenticationInfo = null;

        if (token instanceof WxUserIdToken) {
		    WxUserIdToken userToken = (WxUserIdToken) token;
		    UserInfo userInfoByAcount = userInfoService.getUserInfoByAcount(userToken.getUserId());
		    authenticationInfo = new SimpleAuthenticationInfo(
				    userInfoByAcount, //用户名
				    userInfoByAcount.getPassword(), //密码
				    ByteSource.Util.bytes(userInfoByAcount.getCredentialsSalt()),
				    getName()  //realm name
		    );
	    }else
        if (token instanceof UsernamePasswordToken) {
            //获取用户的输入的账号.
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
            String username = (String) usernamePasswordToken.getUsername();
            String password = String.valueOf(usernamePasswordToken.getPassword());

            //通过username从数据库中查找 User对象，如果找到，没找到.
            //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
            UserInfo userInfo = userInfoService.checkUserInfo(username, password);
            if (userInfo == null) {
                return null;
            }
            if (userInfo.getEnable() == 2) {
                //账户冻结
                throw new LockedAccountException();
            }

            setSession("UserInfoLogin", userInfo);

            authenticationInfo = new SimpleAuthenticationInfo(
                    userInfo, //用户名
                    userInfo.getPassword(), //密码
                    ByteSource.Util.bytes(userInfo.getCredentialsSalt()),
                    getName()  //realm name
            );
        } else if (token instanceof WxUserIdToken) {
            WxUserIdToken userToken = (WxUserIdToken) token;
            UserInfo userInfoByAcount = userInfoMapper.getUserInfoByAcount(userToken.getUserId());
            authenticationInfo = new SimpleAuthenticationInfo(
                    userInfoByAcount,
                    userInfoByAcount.getPassword(),
                    ByteSource.Util.bytes(userInfoByAcount.getCredentialsSalt()),
                    getName()
            );
        }
        return authenticationInfo;
    }
    /**
     * �����¼��
     */
    @SuppressWarnings("unused")
    private void setSession(Object key, Object value){
        Session session = getSession();
        if(null != session){
            session.setAttribute(key, value);
        }else{
            log.error("��ȡhttpServletSessionʧ�ܣ�");
        }
    }

    public Session getSession(){
        try{
            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null){
                session = subject.getSession();
            }
            if (session != null){
                return session;
            }
        }catch (InvalidSessionException e){
            log.error(e + "shiro Ȩ����֤���HttpServletSessionʧ�ܣ�");
        }
        return null;
    }

}
