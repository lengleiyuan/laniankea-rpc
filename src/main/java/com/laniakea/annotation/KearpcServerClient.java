package com.laniakea.annotation;

import java.lang.annotation.*;

/**
 * @author luochang
 * @version KearpcServerClient.java, v 0.1 2019年05月31日 14:50 luochang Exp
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@KearpcClient
@KearpcServer
public @interface KearpcServerClient {
}
