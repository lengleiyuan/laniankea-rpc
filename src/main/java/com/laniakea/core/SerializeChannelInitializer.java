package com.laniakea.core;

import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author wb-lgc489196
 * @version SerializeChannelInitializer.java, v 0.1 2019年06月21日 10:11 wb-lgc489196 Exp
 */
public class SerializeChannelInitializer extends ChannelInitializer {

    private KearpcSerializeProtocol protocol;

    private AbstractMassgeContextWrapper wrapper;

    public SerializeChannelInitializer buildSerialize(KearpcSerializeProtocol protocol) {
        this.protocol = protocol;
        return this;
    }
    public SerializeChannelInitializer buildHandle(AbstractMassgeContextWrapper wrapper){
        this.wrapper = wrapper;
        return this;
    }
    @Override
    protected void initChannel(Channel ch)  {
        ChannelPipeline pipeline = ch.pipeline();
        SelectorProtocol selectorProtocol = new SelectorProtocol(pipeline);
        selectorProtocol.selectSerialize(protocol,wrapper);
        pipeline.addLast(wrapper);
    }
}
