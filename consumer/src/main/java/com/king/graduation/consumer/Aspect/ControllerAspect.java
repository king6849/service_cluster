package com.king.graduation.consumer.Aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author king
 * @date 2020/11/19
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect {

    @Pointcut("execution(public * com.king.graduation.consumer.Controller.*.*(..))")
    public void consumerControllerPointCut() {
    }

    @Before(value = "consumerControllerPointCut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        log.info("=================RequestInfo===============");
        log.info("URL:{}", request.getRequestURL());
        log.info("HttpMethod:{}", request.getMethod());
        log.info("ControllerMethod:{}", joinPoint.getSignature().getName());
        log.info("Params:{}", Arrays.toString(joinPoint.getArgs()));
    }


    @AfterThrowing(throwing = "ex", pointcut = "consumerControllerPointCut()", argNames = "ex")
    public void throwss(Throwable ex) {
        log.error("具体异常:{}", ex.toString());
    }

    @AfterReturning(returning = "var", pointcut = "consumerControllerPointCut()")
    public void after(Object var) {
        log.info("=================ResponseInfo===============");
        log.info("Response:{}", JSON.toJSONString(var));
    }

}
