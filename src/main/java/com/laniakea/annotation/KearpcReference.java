package com.laniakea.annotation;


import com.laniakea.config.KearpcConstants;

import java.lang.annotation.*;

/**
 * @author luochang
 * @version KearpcReference.java, v 0.1 2019年05月29日 17:40 luochang Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KearpcReference {


    String address();

    String interfaceName();

    String protocol() default KearpcConstants.PROTOSTUFFSERIALIZE;


}
