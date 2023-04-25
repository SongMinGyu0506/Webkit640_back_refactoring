package com.webkit640.backend.config.aspect.log;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;


public interface LogService {
    String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl);
    String getRequestUrl(JoinPoint joinPoint, Class<?> cls);
    Map<?,?> params(JoinPoint joinPoint);

}
