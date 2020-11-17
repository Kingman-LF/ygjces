package com.xwkj.ygjces.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis区分数据库环境的配置类
 * @author 张永辉
 */
@Configuration
public class MybatisConfig {

    @Bean
    public DatabaseIdProvider getDatabaseIdProvider(){
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle","oracle");
        properties.setProperty("MySQL","mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}
