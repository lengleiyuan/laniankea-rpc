package com.laniakea.serialize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author luochang
 * @version MessageCodecKit.java, v 0.1 2019年05月30日 12:51 luochang Exp
 */
public interface MessageCodecKit {

    final static int MESSAGE_LENGTH = 4;

    void encode(final ByteBuf out, final Object message) throws IOException;

    Object decode(byte[] body) throws IOException;
}
