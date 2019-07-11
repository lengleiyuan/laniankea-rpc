package com.laniakea.core;

/**
 * @author luochang
 * @version ControllerEventProxy.java, v 0.1 2019年07月09日 16:30 luochang Exp
 */
public interface MessageProxyInterceptor {


    void beforeMessage(String address) throws Throwable;


    void afterMessage(String address, Object obj) throws Throwable;

}
