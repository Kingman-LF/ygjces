package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.utils.MD5Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);

    /**
     * 登录方法
     * @param userInfo
     * @param response
     * @return
     */
    @PostMapping("/loginUserInfo")
    @ResponseBody
    public ResponseResult login(UserInfo userInfo, HttpServletResponse response){
        try {
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(userInfo.getAccount());
            String password = MD5Util.getMD5(userInfo.getPassword());
            token.setPassword(password.toCharArray());
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                subject.login(token);
            }
            return ResponseResult.success();
        }catch (UnknownAccountException e){
            log.error("用户登录失败！：", e);
            return ResponseResult.failure("用户名或者密码错误！");
        } catch (LockedAccountException e){
            log.error("用户登录失败！：", e);
            return ResponseResult.failure("当前用户已经不存在！");
        }

    }

    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
        }
        return "redirect:login";
    }

    /**
     * 去往没有权限展示页面
     * @return
     */
    @GetMapping("/unauth")
    public ModelAndView toUnauth(){
        return new ModelAndView("unauth");
    }

    /**
     * 去往web的登录页面
     * @return
     */
    @GetMapping(value = {"/login", "/"})
    public ModelAndView toLogin(){
        return new ModelAndView("login");
    }
}
