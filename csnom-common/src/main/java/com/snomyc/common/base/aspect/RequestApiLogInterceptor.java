
package com.snomyc.common.base.aspect;

import com.alibaba.fastjson.JSON;
import com.snomyc.common.base.security.manager.TokenManager;
import com.snomyc.common.util.HttpHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 类名称：ApiAccessVerifyInterceptor<br>
 * 类描述：API访问权限验证<br>
 * @version v1.0
 *
 */
@Component
public class RequestApiLogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestApiLogInterceptor.class);
    @Autowired
    private TokenManager tokenManager;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            if (!(handler instanceof HandlerMethod)) {
                return;
            }
            HandlerMethod method = (HandlerMethod) handler;
            String methodName = method.getMethod().getName();
            if (!"error".equals(methodName)) {
                //获取请求api的设备信息
                MDC.clear();
                MDC.put("phoneimei",webRequest.getHeader("Phoneimei"));
                MDC.put("phonesys",webRequest.getHeader("phonesys"));
                MDC.put("phonetype",webRequest.getHeader("phonetype"));
                MDC.put("version",webRequest.getHeader("version"));
                MDC.put("platform",webRequest.getHeader("platform"));
                MDC.put("build",webRequest.getHeader("build"));
                MDC.put("apiUrl",request.getRequestURI().replaceAll(request.getContextPath(), ""));
                MDC.put("methodName",methodName);
                String accessToken =webRequest.getParameter("accessToken");
                MDC.put("accessToken",accessToken);
                String userId ="";
                if(StringUtils.isNotEmpty(accessToken)){
                    userId=tokenManager.getUserId(accessToken);
                }
                MDC.put("userId",userId);
                MDC.put("requestMap",JSON.toJSONString(request.getParameterMap()));

                try {
                    ApiOperation apiOperation = method.getMethod().getAnnotation(ApiOperation.class);
                    MDC.put("apiName",apiOperation.value());
                    String postBody ="";
                    if ("POST".equalsIgnoreCase(apiOperation.httpMethod())) {
                        postBody= HttpHelper.getBodyString(request);
                    }
                    MDC.put("postBody",postBody);
                } catch (Exception ignored) {

                }
                logger.error(MDC.getCopyOfContextMap().toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
