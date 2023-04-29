package com.webkit640.backend.config.aspect.log;

import com.webkit640.backend.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@Aspect
public class UserLogAspect {

    LogService logService;
    MemberService memberService;
    @Autowired
    public UserLogAspect(LogService logService, MemberService memberService) {
        this.logService = logService;
        this.memberService = memberService;
    }

    //@Pointcut("within(com.webkit640.backend.controller.MemberController)")
    @Pointcut("execution(* com.webkit640.backend.controller.*.*(..,!int))")
    public void noAuthUserRequest(){}

    @Pointcut("execution(* com.webkit640.backend.controller.*.*(int,..))")
    public void authUserRequest(){}

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

    @Around("authUserRequest()")
    public Object beforeAuthUserLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> cls = joinPoint.getTarget().getClass();
        Object result;
        Map<?,?> targetParameter = logService.params(joinPoint);
        String email = memberService.getEmailById((int)targetParameter.get("id"));
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            return result;
        } finally {
            log.info("ENTER Member: "+email+"\t"+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint));
        }
    }
}
