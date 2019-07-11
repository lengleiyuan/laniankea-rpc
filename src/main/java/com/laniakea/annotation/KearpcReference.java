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
public @interface KearpcReference {

    String uniqueId();

    String protocol() default "PROTOSTUFFSERIALIZE";  //非订阅需要指定点对点连接

    String address() default ""; //非订阅需要指定点对点连接

    boolean isSubscribe() default true;

}
