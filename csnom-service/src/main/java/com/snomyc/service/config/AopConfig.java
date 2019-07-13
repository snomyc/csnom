package com.snomyc.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author yangcan
 * 类描述:切面配置
 * 创建时间:2018年5月29日 下午5:38:19

 */
@Configuration
@ComponentScan("com.snomyc.service") //扫描com.burning.aop所有的bean
@EnableAspectJAutoProxy //开启Spring对aop的支持
public class AopConfig {

}
