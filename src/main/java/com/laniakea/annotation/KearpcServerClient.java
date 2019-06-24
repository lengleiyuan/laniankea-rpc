package com.laniakea.annotation;

import java.lang.annotation.*;

/**
 * @author wb-lgc489196
 * @version KearpcServerClient.java, v 0.1 2019年05月31日 14:50 wb-lgc489196 Exp
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@KearpcClient
@KearpcServer
public @interface KearpcServerClient {
}
