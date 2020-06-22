package com.snomyc.api.config.swagger;

import com.snomyc.common.base.aspect.RequestApiLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 
 * 类名称：注入RequestApiLogInterceptor
 * 类描述：S
 * @version v1.0
 */
@Configuration
class RequestApiLogInterceptorConfig  extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestApiLogInterceptor requestApiLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(requestApiLogInterceptor);
    }
    
}
