package com.live.tip.audience;

import com.live.tip.audience.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.live.tip")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.live.tip.audience", defaultConfiguration = FeignConfig.class)
@MapperScan("com.live.tip.audience.mapper")
@EnableScheduling
public class AudienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudienceApplication.class, args);
    }
}
