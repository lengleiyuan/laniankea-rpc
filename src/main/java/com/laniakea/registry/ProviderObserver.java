package com.laniakea.registry;

import com.laniakea.executor.MassageClientExecutor;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.Map;

import static com.laniakea.kit.LaniakeaKit.*;

/**
 * @author luochang
 * @version ProviderObserver.java, v 0.1 2019年07月04日 15:19 luochang Exp
 */
public class ProviderObserver {



    public void add(String path) {
        String address = buildAdress(path);
        Map<String,Channel> channels = MassageClientExecutor.ME.getChannel();
        if (!channels.containsKey(address)) {
            KearpcSerializeProtocol protocol = KearpcSerializeProtocol.valueOf(buildProtocal(path));
            InetSocketAddress socketAddress = new InetSocketAddress(ip(address), port(address));
            MassageClientExecutor.ME.setClientProperties(socketAddress, protocol).start();
        }

    }
    public void delete(String path){
        Map<String,Channel> channels = MassageClientExecutor.ME.getChannel();
        String address = buildAdress(path);
        Channel channel = channels.remove(address);
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public void update(String path){
        delete(path);
        add(path);
    }
}
