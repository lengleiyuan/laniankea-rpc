package com.laniakea.core;

/**
 * @author wb-lgc489196
 * @version ControllerEventProxy.java, v 0.1 2019年07月09日 16:30 wb-lgc489196 Exp
 */
public interface MessageProxyInterceptor {


    void beforeMessage(String address) throws Throwable;


    void afterMessage(String address, Object obj) throws Throwable;

}
