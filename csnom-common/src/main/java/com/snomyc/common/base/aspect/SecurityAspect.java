package com.snomyc.common.base.aspect;

import java.lang.reflect.Method;

import com.snomyc.common.base.security.annotation.ModifySecurity;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snomyc.common.base.security.annotation.IgnoreSecurity;
import com.snomyc.common.base.security.exception.TokenException;
import com.snomyc.common.base.security.manager.TokenManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Autowired
    private TokenManager tokenManager;
    private String tokenName = "accessToken";

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void methodPointcut(){}

    //通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为
//    @Around("execution(* com.snomyc.controller..*.*(..))") //拦截com.snomyc包及子包下面的所有类中的所有方法，返回类型任意，方法参数任意
//    public Object around(ProceedingJoinPoint pjp) throws Throwable{
//    	// 从切点上获取目标方法
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        Method method = methodSignature.getMethod();
//
//        String uri = WebContext.getRequest().getRequestURI().replaceAll(WebContext.getRequest().getContextPath(), "");
//
//        // 若目标方法忽略了安全性检查或不为ajax请求，则直接调用目标方法
//        if (method.isAnnotationPresent(IgnoreSecurity.class)  || (!StringUtils.startsWith(uri, "/api/"))) {
//            return pjp.proceed();
//        }
//        // 从 request header 中获取当前 token
//        String token = StringUtils.isBlank(WebContext.getRequest().getHeader(tokenName)) ? WebContext.getRequest().getParameter("accessToken") : WebContext.getRequest().getHeader(tokenName);
//        // 检查 token 有效性
//        if ( token ==null || !tokenManager.checkToken(token) ) {
//            String message = String.format("token [%s] is invalid", token);
//            throw new TokenException(message);
//        }
//        // 调用目标方法
//        return pjp.proceed();
//    }

    /**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("methodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp)throws Throwable{
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (method.isAnnotationPresent(IgnoreSecurity.class)  ) {
            logger.info("直接过滤url：{}", request.getRequestURI());
            return pjp.proceed();
        }

        String methodName = method.getName(); //获取被拦截的方法名

        String url = request.getRequestURI().replaceAll(request.getContextPath(), "");
        logger.info("请求url：{}", url);
        if ( !StringUtils.startsWith(url, "/wxapi/") && !StringUtils.startsWith(url, "/api/")) {
            return pjp.proceed();
        }
        String token = StringUtils.isBlank(request.getHeader("accessToken")) ? request.getParameter("accessToken") : request.getHeader("accessToken");

        logger.info("请求方法：{}", methodName);
        logger.info("，请求token：{}", token);
        // 检查 token 有效性
        if ( token ==null || !tokenManager.checkToken(token) ) {
            String message = String.format("token [%s] is invalid", token);
            throw new TokenException(message);
        }
        if(method.isAnnotationPresent(ModifySecurity.class)){
            String requestsNum = tokenManager.getString(token+url);
            if(requestsNum!=null){
                String message = String.format("请求太频繁 [%s] 请稍后", token+url);
                throw new RuntimeException(message);
            }
            tokenManager.setString(token+url, "1",2);
        }
        // 调用目标方法
        return pjp.proceed();
    }

}
