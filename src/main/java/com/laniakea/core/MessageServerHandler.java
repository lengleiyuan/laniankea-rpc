package com.laniakea.core;

import com.laniakea.controller.AsyncPushController;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author luochang
 * @version MessageServerHandler.java, v 0.1 2019年05月30日 14:19 luochang Exp
 */
public class MessageServerHandler extends AbstractMassgeHandleWrapper {


    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        LaniakeaRequest request = (LaniakeaRequest) msg;
        AsyncPushController push = new AsyncPushController(request,ctx);
        push.writeAndFlush();
    }


}
