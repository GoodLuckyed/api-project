package com.lucky.apibackend.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lucky
 * @date 2024/4/18
 * @description 自定义身份校验注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 有任何一个角色
     * @return
     */
    String[] anyRole() default "";

    /**
     * 必须有某个角色
     * @return
     */
    String mustRole() default "";
}
