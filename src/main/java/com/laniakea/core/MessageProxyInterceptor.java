package com.laniakea.core;

import io.netty.channel.Channel;


/**
 * @author luochang
 * @version MessageProxyInterceptor.java, v 0.1 2019年07月09日 16:30 luochang Exp
 */
public interface MessageProxyInterceptor<T> {


    Channel beforeMessage() throws Throwable;


    T afterMessage(T t, Channel channel) throws Throwable;


}
