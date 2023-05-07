package com.webkit640.backend.config.annotation;

import java.lang.annotation.*;


/**
 * 해당 어노테이션은 관리자 기능을 동작할때, 사용 유저가 관리자인지 아닌지 체크하는 Marker Annotation
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Admin {
}
