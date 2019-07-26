package com.laniakea.core;

import io.netty.channel.Channel;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wb-lgc489196
 * @version MessageBalanceProxyInterceptor.java, v 0.1 2019年07月12日 10:24 wb-lgc489196 Exp
 */
public class MessageBalanceProxyInterceptor<T> implements MessageProxyInterceptor<T>{

    private List<Channel> channels;

    private AtomicInteger concurrentQuantity = new AtomicInteger(0);

    private Semaphore semaphore;


    public MessageBalanceProxyInterceptor(List<Channel> channels, Semaphore semaphore){
        this.semaphore = semaphore;
        this.channels = channels;
    }


    @Override
    public Channel beforeMessage() throws Throwable {
        semaphore.acquire();
        return channels.get(concurrentQuantity.getAndIncrement() % channels.size());
    }


    @Override
    public T afterMessage(T t, Channel channel)  {
        semaphore.release();
        return t;
    }


}
