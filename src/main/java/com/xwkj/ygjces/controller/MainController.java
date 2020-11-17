package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.LoginUserInfoManager;
import com.xwkj.ygjces.model.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


/**
 * 去往首页展示页面
 */
@RestController
public class MainController {

    @GetMapping("/welcome")
    public ModelAndView welcome(){
        return new ModelAndView("welcome");
    }

    @GetMapping("/tomain")
    public ModelAndView toMain(HttpSession session){
        UserInfo userInfo = LoginUserInfoManager.getUserInfo();
        if(userInfo != null) {
            return new ModelAndView("index").addObject("userInfo", userInfo);
        }else{
            SecurityUtils.getSubject().logout();
            return new ModelAndView("login");
        }
    }
}
