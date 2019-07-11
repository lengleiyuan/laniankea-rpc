package com.laniakea.core;

import com.laniakea.annotation.KearpcReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author wb-lgc489196
 * @version MessageEventAdvisor.java, v 0.1 2019年07月09日 16:25 wb-lgc489196 Exp
 */
public class MessageEventAdvice<T> implements InvocationHandler {

    private MessageProxyInterceptor interceptor;

    private MessageProxy messageProxy;

    private String address;


    public MessageEventAdvice(MessageProxy messageProxy,MessageProxyInterceptor interceptor, String address){
        this.messageProxy = messageProxy;
        this.interceptor = interceptor;
        this.address = address;
    }


    @Override
    public T invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        KearpcReference reference = method.getDeclaringClass()
                .getAnnotation(KearpcReference.class);

        request.setClassName(reference.uniqueId());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);

        interceptor.beforeMessage(address);

        T t = (T) messageProxy.sendMessage(request);

        interceptor.afterMessage(address,t);

        return t;
    }
}
