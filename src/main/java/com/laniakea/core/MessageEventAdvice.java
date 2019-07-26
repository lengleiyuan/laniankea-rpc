package com.laniakea.core;

import com.laniakea.annotation.KearpcReference;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author luochang
 * @version MessageEventAdvisor.java, v 0.1 2019年07月09日 16:25 luochang Exp
 */
public class MessageEventAdvice<T> implements InvocationHandler {

    private MessageProxyInterceptor interceptor;

    private MessageProxy messageProxy;




    public MessageEventAdvice(MessageProxy messageProxy,MessageProxyInterceptor interceptor){
        this.messageProxy = messageProxy;
        this.interceptor = interceptor;
    }


    @Override
    public T invoke(Object proxy, Method method, Object[] args) throws Throwable {

        LaniakeaRequest request = new LaniakeaRequest();
        request.setMessageId(UUID.randomUUID().toString());
        Class<?> declaringClass = method.getDeclaringClass();
        KearpcReference reference = declaringClass.getAnnotation(KearpcReference.class);

        request.setClassName(reference.uniqueId());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);

        Channel channel = interceptor.beforeMessage();

        T t = (T) messageProxy.sendMessage(request,channel);

        return (T) interceptor.afterMessage(t, channel);

    }
}
