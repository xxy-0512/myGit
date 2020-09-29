package com.xxy.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: xxy
 * @Description: SpringBoot启动类
 * @Date: Created in 11:16 2020/9/15
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.xxy.wallet")
@ServletComponentScan(basePackages = "com.xxy.wallet.filter")
@EnableScheduling
public class StartUp {
    public static void main(String[] args) {
        SpringApplication.run(StartUp.class, args);
    }
}
