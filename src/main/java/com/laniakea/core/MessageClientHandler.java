package com.laniakea.core;

import com.laniakea.cache.CallbackCache;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author luochang
 * @version MessageClientHandler.java, v 0.1 2019年05月30日 12:15 luochang Exp
 */
public class MessageClientHandler extends AbstractMassgeHandleWrapper {


    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        LaniakeaResponse response = (LaniakeaResponse) msg;
        String messageId = response.getMessageId();
        CallbackCache cache = CallbackCache.getCache();
        MessageCallBack callBack = cache.getMassgeCallBack(messageId);
        callBack.finish(response);
        cache.removeCallBack(messageId);

    }

}