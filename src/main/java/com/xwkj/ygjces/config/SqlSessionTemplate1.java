package com.xwkj.ygjces.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * oracle扫描mapper的配置类
 * @author 张永辉
 */
@Configuration
@MapperScan(basePackages = "com.xwkj.ygjces.Mapper_oracle", sqlSessionTemplateRef  = "primarySqlSessionTemplate")
public class SqlSessionTemplate1 {

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/oracle_mapper*/*.xml"));
        return bean.getObject();
    }

    //配置声明式事务管理器
    @Bean(name = "primaryTransactionManager")
    @Primary
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(
            @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
