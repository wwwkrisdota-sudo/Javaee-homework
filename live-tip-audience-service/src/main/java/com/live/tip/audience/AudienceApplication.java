package com.live.tip.audience;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.live.tip")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.live.tip.audience")
@MapperScan("com.live.tip.audience.mapper")
public class AudienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudienceApplication.class, args);
    }
}
