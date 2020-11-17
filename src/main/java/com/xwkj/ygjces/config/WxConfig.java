package com.xwkj.ygjces.config;

import com.xwkj.ygjces.model.MyWxConfigStorage;
import com.xwkj.ygjces.model.WxConfigStorage;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.service.impl.WxServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 * @author 张永辉
 */
@Configuration
public class WxConfig {

    @Bean
    public WxService myWxService(MyWxConfigStorage myWxConfigStorage){
        WxServiceImpl wxService = new WxServiceImpl();
        wxService.setWxCpConfigStorage(myWxConfigStorage);
        return wxService;
    }

    @Bean
    public WxService wxService(WxConfigStorage wxConfigStorage){
        WxServiceImpl wxService = new WxServiceImpl();
        wxService.setWxCpConfigStorage(wxConfigStorage);
        return wxService;
    }


}
