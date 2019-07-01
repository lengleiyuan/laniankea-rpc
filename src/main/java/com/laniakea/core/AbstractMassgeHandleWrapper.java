package com.laniakea.core;

import io.netty.channel.*;

/**
 * @author luochang
 * @version AbstractMassgeContextWrapper.java, v 0.1 2019年05月30日 14:13 luochang Exp
 */
@ChannelHandler.Sharable
public abstract class AbstractMassgeHandleWrapper extends ChannelInboundHandlerAdapter {


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg){
        handleMessage(ctx,msg);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }


    abstract void handleMessage(ChannelHandlerContext ctx, Object msg);


}
