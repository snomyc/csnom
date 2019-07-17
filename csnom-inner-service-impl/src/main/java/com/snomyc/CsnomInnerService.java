package com.snomyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.snomyc"})
@EnableScheduling //定时任务
public class CsnomInnerService extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CsnomInnerService.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CsnomInnerService.class, args);
    }
}

