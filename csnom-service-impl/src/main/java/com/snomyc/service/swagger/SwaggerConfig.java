package com.snomyc.service.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author yangcan
 * 类描述:用@Configuration注解该类，等价于XML中配置beans；用@Bean标注方法等价于XML中配置bean。
 * Application启动类加上@EnableSwagger2
 * 创建时间:2018年5月24日 下午7:39:01

 */
@Configuration
public class SwaggerConfig {
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.snomyc"))
                .paths(PathSelectors.any())
                .build();
    }
	
	private ApiInfo apiInfo() {
        return new ApiInfo(
                "前端APP接口文档",
                "前端Restful接口文档",
                "1.0",
                "",
                new Contact("耳可火山", "https://www.sharingschool.com/", "snomyc@qq.com"),
                "LICENSE-2.0",
                "https://github.com/snomyc/csnom");
    }

}
