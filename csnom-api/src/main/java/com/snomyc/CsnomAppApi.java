package com.snomyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.snomyc"})
@EnableSwagger2 //加载swagger
@EnableScheduling //定时任务
public class CsnomAppApi extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CsnomAppApi.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CsnomAppApi.class, args);
    }
}

