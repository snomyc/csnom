package com.snomyc.api.config.aspect;

import com.snomyc.common.base.aspect.WebContext;
import com.snomyc.common.base.security.annotation.IgnoreSecurity;
import com.snomyc.common.base.security.exception.TokenException;
import com.snomyc.common.base.security.manager.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 
 * 类名称：SecurityAspect<br>
 * 类描述：用于检查 token 的切面<br>
 * @version v1.0
 *@Aspect
 */
@Aspect
@Component
public class SecurityAspect {

    @Autowired
    private TokenManager tokenManager;
    private String tokenName = "accessToken";

    //通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为
    @Around("execution(* com.snomyc.api.common.*.*(..))") //拦截com.snomyc包及子包下面的所有类中的所有方法，返回类型任意，方法参数任意
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
    	// 从切点上获取目标方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        String uri = WebContext.getRequest().getRequestURI().replaceAll(WebContext.getRequest().getContextPath(), "");

        // 若目标方法忽略了安全性检查或不为ajax请求，则直接调用目标方法
        //如果接口是/api开头的则不校验
        if (method.isAnnotationPresent(IgnoreSecurity.class)  || StringUtils.startsWith(uri, "/api/")) {
            return pjp.proceed();
        }
        // 从 request header 中获取当前 token
        String token = StringUtils.isBlank(WebContext.getRequest().getHeader(tokenName)) ? WebContext.getRequest().getParameter("accessToken") : WebContext.getRequest().getHeader(tokenName);
        // 检查 token 有效性
        if ( token ==null || !tokenManager.checkToken(token) ) {
            String message = String.format("token [%s] is invalid", token);
            throw new TokenException(message);
        }
        // 调用目标方法
        return pjp.proceed();
    }

}
