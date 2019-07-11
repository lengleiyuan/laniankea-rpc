package com.laniakea.controller;

import com.laniakea.cache.CallbackCache;
import com.laniakea.core.MessageCallBack;
import com.laniakea.core.MessageProxy;
import com.laniakea.core.MessageProxyInterceptor;
import com.laniakea.core.MessageRequest;
import com.laniakea.executor.MassageClientExecutor;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author luochang
 * @version PushController.java, v 0.1 2019年06月20日 10:31 luochang Exp
 */
public class AsyncPullController<T> implements MessageProxyInterceptor, MessageProxy {

    //volatile
    private AtomicReference<Channel> channel = new AtomicReference<>();

    @Override
    public void beforeMessage(String address) {

        Channel ch = MassageClientExecutor.ME.get(address);

        channel.set(ch);

    }

    @Override
    public T sendMessage(MessageRequest request) throws Throwable {

        Channel ch = channel.get();
        MessageCallBack callBack = new MessageCallBack(ch);
        CallbackCache.getCache().putMassgeCallBack(request.getMessageId(), callBack);
        ch.writeAndFlush(request);
        return (T) callBack.create();
    }



    @Override
    public void afterMessage(String address, Object t) {

    }





}
