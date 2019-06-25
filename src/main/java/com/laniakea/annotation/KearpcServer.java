package com.laniakea.annotation;

import java.lang.annotation.*;

/**
 * @author luochang
 * @version KearpcServer.java, v 0.1 2019年05月31日 14:49 luochang Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KearpcServer {

}
