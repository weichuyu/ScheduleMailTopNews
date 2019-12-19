package com.yu.tools.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动入口，配置包根路径
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.yu.tools")
public class CronExecuteApp {
    public static void main(String[] args) throws Exception {
        //尝试提交点东西
        //直接在github上编辑
        SpringApplication.run(CronExecuteApp.class, args);
    }
}
