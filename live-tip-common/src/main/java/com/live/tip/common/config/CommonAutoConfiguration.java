package com.live.tip.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 公共模块自动配置
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.live.tip.common")
public class CommonAutoConfiguration {
}
