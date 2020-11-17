package com.xwkj.ygjces.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 回显图片路径配置类
 * @author zyh
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //同时需要在shiro中配置以下，否则会被拦截
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/Users/zhang/Company/upload/");
//        registry.addResourceHandler("/temporary/**").addResourceLocations("file:D:/Users/zhang/Company/temporary/");
        registry.addResourceHandler(new String[] { "/upload/**" }).addResourceLocations(new String[] { "file:C:/xwkj/upload/" });
        registry.addResourceHandler(new String[] { "/temporary/**" }).addResourceLocations(new String[] { "file:C:/xwkj/temporary/" });
    }
}
