package com.webkit640.backend.config.aspect.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class NoAuthUserLogAspect {

    LogService logService;
    @Autowired
    public NoAuthUserLogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("within(com.webkit640.backend.controller.TestController)")
    public void noAuthUserRequest(){}

    @Around("noAuthUserRequest()")
    public Object beforeNoAuthUserLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> cls = joinPoint.getTarget().getClass();
        Object result;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            return result;
        } finally {
            log.info("ENTER "+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint));
        }
    }
}
