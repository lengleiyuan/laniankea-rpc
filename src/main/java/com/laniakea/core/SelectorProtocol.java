package com.laniakea.core;

import com.laniakea.serialize.KearpcSerializeProtocol;
import com.laniakea.serialize.MessageCodecKit;
import com.laniakea.serialize.MessageDecoder;
import com.laniakea.serialize.MessageEncoder;
import com.laniakea.serialize.hessian.HessianCodecKit;
import com.laniakea.serialize.kryo.KryoCodecKit;
import com.laniakea.serialize.kryo.KryoPoolFactory;
import com.laniakea.serialize.protostuff.ProtostuffCodecKit;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author luochang
 * @version SelectorSerializeProtocol.java, v 0.1 2019年06月18日 17:05 luochang Exp
 */
public class SelectorProtocol {

    private ChannelPipeline pipeline;

    private KearpcSerializeProtocol protocol;

    public SelectorProtocol(ChannelPipeline pipeline){
        this.pipeline = pipeline;
    }

    private ChannelPipeline JdkNativeChannel(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecKit.MESSAGE_LENGTH, 0, MessageCodecKit.MESSAGE_LENGTH))
                .addLast(new LengthFieldPrepender(MessageCodecKit.MESSAGE_LENGTH)).addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        return pipeline;
    }

    private ChannelPipeline KryoChannel(ChannelPipeline pipeline) {
        KryoCodecKit kit = new KryoCodecKit(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new MessageEncoder(kit)).addLast(new MessageDecoder(kit));
        return pipeline;
    }

    private ChannelPipeline HessianChannel(ChannelPipeline pipeline) {
        HessianCodecKit kit = new HessianCodecKit();
        pipeline.addLast(new MessageEncoder(kit)).addLast(new MessageDecoder(kit));
        return pipeline;
    }

    private ChannelPipeline ProtostuffChannel(ChannelPipeline pipeline, AbstractMassgeHandleWrapper wrapper) {
        ProtostuffCodecKit kit = new ProtostuffCodecKit();
        if(wrapper instanceof MessageClientHandler){
            kit.setGenericClass(MessageResponse.class);
        }
        if(wrapper instanceof MessageServerHandler){
            kit.setGenericClass(MessageRequest.class);
        }
        pipeline.addLast(new MessageEncoder(kit));
        pipeline.addLast(new MessageDecoder(kit));
        return pipeline;
    }
    public ChannelPipeline selectSerialize(KearpcSerializeProtocol protocol, AbstractMassgeHandleWrapper wrapper){

        switch (protocol) {

            case JDKSERIALIZE:
                return JdkNativeChannel(pipeline);

            case KRYOSERIALIZE:
                return KryoChannel(pipeline);

            case HESSIANSERIALIZE:
                return HessianChannel(pipeline);

            case PROTOSTUFFSERIALIZE:
                return ProtostuffChannel(pipeline,wrapper);

            default:
                return pipeline;

        }
    }
}
