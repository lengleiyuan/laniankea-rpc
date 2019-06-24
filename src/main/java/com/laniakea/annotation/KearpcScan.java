package com.laniakea.annotation;

import java.lang.annotation.*;

/**
 * @author wb-lgc489196
 * @version KearpcScan.java, v 0.1 2019年06月03日 17:38 wb-lgc489196 Exp
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KearpcScan {
    String value();
}
