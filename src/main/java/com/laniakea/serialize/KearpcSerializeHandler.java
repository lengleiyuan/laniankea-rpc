package com.laniakea.serialize;

import io.netty.channel.ChannelPipeline;

/**
 * @author wb-lgc489196
 * @version KearpcClientHandler.java, v 0.1 2019年05月30日 12:39 wb-lgc489196 Exp
 */
@FunctionalInterface
public interface KearpcSerializeHandler {
    void handle(ChannelPipeline pipeline);
}
