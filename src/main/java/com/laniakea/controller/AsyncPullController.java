package com.laniakea.controller;

import com.laniakea.cache.CallbackCache;
import com.laniakea.core.MessageCallBack;
import com.laniakea.core.MessageProxy;
import com.laniakea.core.MessageRequest;
import io.netty.channel.Channel;


/**
 * @author luochang
 * @version PushController.java, v 0.1 2019年06月20日 10:31 luochang Exp
 */
public class AsyncPullController<T> implements MessageProxy {

    @Override
    public T sendMessage(MessageRequest request, Channel channel) throws Throwable {
        MessageCallBack callBack = new MessageCallBack(channel);
        CallbackCache.getCache().putMassgeCallBack(request.getMessageId(), callBack);
        channel.writeAndFlush(request);
        return (T) callBack.create();
    }


}
