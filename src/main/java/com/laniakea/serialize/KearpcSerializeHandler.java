package com.laniakea.serialize;

import io.netty.channel.ChannelPipeline;

/**
 * @author luochang
 * @version KearpcClientHandler.java, v 0.1 2019年05月30日 12:39 luochang Exp
 */
@FunctionalInterface
public interface KearpcSerializeHandler {
    void handle(ChannelPipeline pipeline);
}
