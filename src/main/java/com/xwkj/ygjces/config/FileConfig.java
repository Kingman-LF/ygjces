package com.xwkj.ygjces.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 上传文件配置类
 * @author zyh
 */
@Configuration
public class FileConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("5120KB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("204800KB");
        return factory.createMultipartConfig();
    }

}
