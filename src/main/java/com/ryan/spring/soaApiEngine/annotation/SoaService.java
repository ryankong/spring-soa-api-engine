package com.ryan.spring.soaApiEngine.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Controller;

/**
 * soa服务接口声明注解，复用@Controller的http功能
 * @author Ryan Kong
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Controller
@Documented
public @interface SoaService {
	String value() default "";
}
