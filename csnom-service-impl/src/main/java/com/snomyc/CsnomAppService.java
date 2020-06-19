package com.snomyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.snomyc"})
//开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />，如果@Transactional 注解在类上则所有方法均支持事物，注解在方法上则该方法支持事物
@EnableTransactionManagement
@EnableCaching
public class CsnomAppService extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CsnomAppService.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CsnomAppService.class, args);
    }
}

