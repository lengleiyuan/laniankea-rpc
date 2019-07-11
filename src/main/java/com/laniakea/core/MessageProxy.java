package com.laniakea.core;

/**
 * @author luochang
 * @version MessageProxy.java, v 0.1 2019年07月10日 18:20 luochang Exp
 */
public interface MessageProxy<T> {

    T sendMessage(MessageRequest request) throws Throwable;


}
