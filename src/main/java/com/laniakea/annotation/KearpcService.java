package com.laniakea.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author wb-lgc489196
 * @version KearpcReference.java, v 0.1 2019年05月29日 17:40 wb-lgc489196 Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface KearpcService {

}
