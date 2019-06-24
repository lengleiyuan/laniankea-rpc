package com.laniakea.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wb-lgc489196
 * @version MessageEncoder.java, v 0.1 2019年05月30日 13:38 wb-lgc489196 Exp
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    private MessageCodecKit util;

    public MessageEncoder(final MessageCodecKit util) {
        this.util = util;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        util.encode(out, msg);
    }
}