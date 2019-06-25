package com.laniakea.core;

import com.laniakea.controller.PushController;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author luochang
 * @version MessageServerHandler.java, v 0.1 2019年05月30日 14:19 luochang Exp
 */
public class MessageServerHandler extends AbstractMassgeContextWrapper {



    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        MessageRequest request = (MessageRequest) msg;
        PushController push = new PushController(request,ctx);
        push.writeAndFlush();
    }


}
