package com.xwkj.ygjces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@MapperScan({"com.xwkj.ygjces.Mapper","com.xwkj.ygjces.Mapper_oracle"})
public class YgjcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(YgjcesApplication.class, args);
    }

}
