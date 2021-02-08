package com.yamada.user;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
//@EnableDiscoveryClient
@MapperScan("com.yamada.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);

        try {
            String serverAddr = "111.230.220.64:8848";
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("username", "yamada");
            properties.put("password", "");
            ConfigService configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
