package com.laniakea.core;

/**
 * @author wb-lgc489196
 * @version MessageProxy.java, v 0.1 2019年07月10日 18:20 wb-lgc489196 Exp
 */
public interface MessageProxy<T> {

    T sendMessage(MessageRequest request) throws Throwable;


}
