package com.laniakea.controller;

import com.laniakea.core.MessageCache;
import com.laniakea.core.MessageRequest;
import com.laniakea.core.MessageResponse;
import com.laniakea.exection.KearpcException;
import com.laniakea.executor.MassageServerExecutor;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * @author luochang
 * @version PushController.java, v 0.1 2019年06月21日 18:20 luochang Exp
 */
public class PushController {

    private Logger logger = LoggerFactory.getLogger(PushController.class);

    private MessageRequest request;

    private ChannelHandlerContext ctx;

    public PushController(MessageRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    public MessageResponse call() {
        MessageResponse response = new MessageResponse();
        response.setMessageId(request.getMessageId());
        String className = request.getClassName();
        Object serviceBean = MessageCache.getCache().getHandler(className);
        if(null == serviceBean){
            throw new KearpcException("[Reference.interfaceName] not corresponding to @KearpcService");
        }
        Method method = ReflectionUtils.findMethod(serviceBean.getClass(), request.getMethodName(), request.getTypeParameters());
        if(null == method){
            throw new KearpcException(
                    "This method was not found to Server.class = " + serviceBean.getClass() + "method = " + request.getMethodName()
                            + "typeParameters = " + request.getTypeParameters());
        }
        Object result = ReflectionUtils.invokeMethod(method, serviceBean, request.getParameters());
        response.setResult(result);
        return response;
    }

    public void writeAndFlush(){
        CompletableFuture.supplyAsync(this::call, MassageServerExecutor.ME.threadPoolExecutor).handle(
                ((response, throwable) -> null == response ? new MessageResponse(throwable, ctx, request.getMessageId()) : response))
                .thenAccept(response -> ctx.writeAndFlush(response)
                        .addListener((channelFuture) -> logger.info("Server Send message-id respone:" + request
                                .getMessageId())));
    }

}
