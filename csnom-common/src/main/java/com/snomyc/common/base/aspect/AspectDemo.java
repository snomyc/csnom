package com.snomyc.common.base.aspect;//package com.snomyc.base.aspect;
//
//import java.lang.reflect.Method;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
///**
// * @author yangcan
// * 类描述:切面demo
// * 创建时间:2018年5月29日 下午5:30:24
//
//
//@AfterThrowing:异常通知：在方法抛出异常退出时执行的通知。　　　　　　　
//
//　　　@After 最终通知。当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
//
//　　　@Around：环绕通知：包围一个连接点的通知，如方法调用。这是最强大的一种通知类型。环绕通知可以在方法调用前后完成自定义的行为。它也会选择是否继续执行连接点或直接返回它自己的返回值或抛出异常来结束执行。
//
//　　　　　　环绕通知最麻烦，也最强大，其是一个对方法的环绕，具体方法会通过代理传递到切面中去，切面中可选择执行方法与否，执行方法几次等。
//
//　　　　　　环绕通知使用一个代理ProceedingJoinPoint类型的对象来管理目标对象，所以此通知的第一个参数必须是ProceedingJoinPoint类型，在通知体内，调用ProceedingJoinPoint的proceed()方法会导致后台的连接点方法执行。proceed 方法也可能会被调用并且传入一个Object[]对象-该数组中的值将被作为方法执行时的参数。
// */
//@Aspect
//@Component
//public class AspectDemo {
//
////	@Pointcut("@annotation(com.zh.ch1.aop.Action)") // 3：该注解声明切入点  
////    public void annotationPointCut() {  
////    };  
//    //注解  
//    /*@After("annotationPointCut()") // 4:使用刚声明的切入点(annotationPointCut()) 
//    public void after(JoinPoint joinPoint) { 
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
//        Method method = signature.getMethod(); 
//        Action action = method.getAnnotation(Action.class); 
//        System.out.println("注解式拦截 " + action.name()); // 5：通过反射获取注解的属性值 
//    }*/
//	
//	  //在方法调用之前调用通知
//    @Before("execution(* com.snomyc..*.*(..))") //6:直接拦截方法名  
//    public void before(JoinPoint joinPoint){  
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  
//        Method method = signature.getMethod();  
//        System.out.println("方法规则式拦截,"+method.getName());  
//  
//    }
//    
//      //在方法完成之后调用通知，无论方法执行成功与否
//    @After("execution(* com.snomyc..*.*(..))") //拦截com.snomyc包及子包下面的所有类中的所有方法，返回类型任意，方法参数任意 
//    public void after(JoinPoint joinPoint){  
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  
//        Method method = signature.getMethod();  
//        System.out.println("方法规则式拦截,"+method.getName());  
//  
//    }
//	
//    //通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为
//    @Around("execution(* com.snomyc.controller..*.*(..))") //拦截com.snomyc包及子包下面的所有类中的所有方法，返回类型任意，方法参数任意 
//    public Object around(ProceedingJoinPoint pjp) throws Throwable{  
//        MethodSignature signature = (MethodSignature) pjp.getSignature();  
//        Method method = signature.getMethod();
//        System.out.println("方法规则式拦截"+method.getName());
//        //调用目标方法
//        return pjp.proceed();
//    }
//    
//}
