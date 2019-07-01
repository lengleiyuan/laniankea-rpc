package com.laniakea.controller;

import com.laniakea.annotation.KearpcReference;
import com.laniakea.core.MessageCache;
import com.laniakea.core.MessageCallBack;
import com.laniakea.core.MessageRequest;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author luochang
 * @version PushController.java, v 0.1 2019年06月20日 10:31 luochang Exp
 */
public class PullController<T> implements InvocationHandler {

    private Channel channel;

    public PullController(Channel channel) {
        this.channel = channel;
    }

    @Override
    public T invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        KearpcReference reference = method.getDeclaringClass().getAnnotation(KearpcReference.class);
        request.setClassName(reference.interfaceName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);
        return sendAsynMessage(request);
    }

    public T sendAsynMessage(MessageRequest request) throws Throwable {
        MessageCallBack callBack = new MessageCallBack(channel);
        MessageCache.getCache().putMassgeCallBack(request.getMessageId(), callBack);
        channel.writeAndFlush(request);
        return (T) callBack.create();
    }

}
