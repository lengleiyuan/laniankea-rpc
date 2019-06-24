package com.laniakea.core;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wb-lgc489196
 * @version MessageClientHandler.java, v 0.1 2019年05月30日 12:15 wb-lgc489196 Exp
 */
public class MessageClientHandler extends AbstractMassgeContextWrapper {


    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();
        MessageCache cache = MessageCache.getCache();
        MessageCallBack callBack = cache.getMassgeCallBack(messageId);
        callBack.finish(response);
        cache.removeCallBack(messageId);

    }

}