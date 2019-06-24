package com.laniakea.core;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wb-lgc489196
 * @version MassgeContextWrapper.java, v 0.1 2019年05月30日 14:13 wb-lgc489196 Exp
 */
@ChannelHandler.Sharable
public abstract class AbstractMassgeContextWrapper extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
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
