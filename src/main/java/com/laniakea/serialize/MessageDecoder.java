package com.laniakea.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author luochang
 * @version MessageDecoder.java, v 0.1 2019年05月30日 13:39 luochang Exp
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());


    final public static int MESSAGE_LENGTH = MessageCodecKit.MESSAGE_LENGTH;

    private MessageCodecKit util;

    public MessageDecoder(final MessageCodecKit util) {
        this.util = util;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < MessageDecoder.MESSAGE_LENGTH) {
            return;
        }

        in.markReaderIndex();
        int messageLength = in.readInt();

        if (messageLength < 0) {
            ctx.close();
        }

        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
            return;
        } else {
            byte[] messageBody = new byte[messageLength];
            in.readBytes(messageBody);

            try {
                Object obj = util.decode(messageBody);
                out.add(obj);
            } catch (IOException ex) {
                logger.warn(ex.getMessage(),ex);
            }
        }
    }
}