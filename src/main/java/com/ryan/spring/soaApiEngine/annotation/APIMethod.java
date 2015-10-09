package com.ryan.spring.soaApiEngine.annotation;

 
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface APIMethod {
	/** API的名称(简述) **/
	String name();
	
	/** API的归类，在 OPEN API 中，我们将按照分类进行组织显示，请再APICategory类中注册 **/
	APICategory category() default APICategory.OTHERS;
	
	/** API使用方，如果方法未写明调用方, 则很有可能被清理掉，请再APIUser中注册 **/
	APIUser[] apiUsers();
	
	/** API的版本(我们将根据不同的版本调用不同的API)（未实现） **/
	String version() default "1.0";
	
	/** API的归类，在 OPEN API 中，我们将按照分类进行组织显示 **/
	String desc() default "";
	
	/** API作者\维护者 **/
	String authors();
}
