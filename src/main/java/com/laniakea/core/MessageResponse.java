package com.laniakea.core;


import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author wb-lgc489196
 * @version MessageResponse.java, v 0.1 2019年05月30日 12:22 wb-lgc489196 Exp
 */
public class MessageResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String error;

    private T result;

    public MessageResponse(){}

    public MessageResponse(Throwable throwable, ChannelHandlerContext ctx,String messageId) {
        StackTraceElement stackTraceElement = throwable.getCause().getStackTrace()[0];
        setError(throwable.getMessage().concat(":")
                .concat(stackTraceElement.getClassName()).concat(".")
                .concat(stackTraceElement.getMethodName()));
        this.messageId = messageId;
        ctx.writeAndFlush(this);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
