//Copyright Sun Microsystems Inc. 2004 - 2005.

package com.ryan.spring.soaApiEngine.annotation;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD})
@Retention(RUNTIME)
public @interface APIField {
  
    String value() default "";
}
