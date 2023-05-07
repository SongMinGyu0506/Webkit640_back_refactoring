package com.webkit640.backend.config.aspect.log;

import com.webkit640.backend.config.exception.NoAdminException;
import com.webkit640.backend.service.MemberService;
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
public class AdminAuthorizationAspect {

    private final LogService logService;
    private final MemberService memberService;

    @Autowired
    public AdminAuthorizationAspect(LogService logService, MemberService memberService) {
        this.logService = logService;
        this.memberService = memberService;
    }


    @Around("@annotation(com.webkit640.backend.config.annotation.Admin)")
    public Object adminMemberCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        if (memberService.getMemberById((int)logService.params(joinPoint).get("id")).isAdmin()) {
            return joinPoint.proceed();
        } else {
         throw new NoAdminException("No Administrator");
        }
    }
}
