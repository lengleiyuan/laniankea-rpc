package com.laniakea.annotation;


import java.lang.annotation.*;

/**
 * @author luochang
 * @version KearpcClient.java, v 0.1 2019年05月28日 18:40 luochang Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KearpcClient {

}
