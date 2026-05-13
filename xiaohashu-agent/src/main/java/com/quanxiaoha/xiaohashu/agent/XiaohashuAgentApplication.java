package com.quanxiaoha.xiaohashu.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.quanxiaoha")
public class XiaohashuAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaohashuAgentApplication.class, args);
    }
}
