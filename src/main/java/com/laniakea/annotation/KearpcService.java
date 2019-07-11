package com.laniakea.annotation;


import java.lang.annotation.*;

/**
 * @author luochang
 * @version KearpcReference.java, v 0.1 2019年05月29日 17:40 luochang Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KearpcService {

    boolean isRegistry() default true;

}
