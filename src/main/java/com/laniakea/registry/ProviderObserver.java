package com.laniakea.registry;

import com.laniakea.cache.AddressCache;
import com.laniakea.core.MessageClientInfo;
import com.laniakea.executor.MassageClientExecutor;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static com.laniakea.kit.LaniakeaKit.*;

/**
 * @author luochang
 * @version ProviderObserver.java, v 0.1 2019年07月04日 15:19 luochang Exp
 */
public class ProviderObserver {



    public void add(String uniqueId,String path) {


        CopyOnWriteArrayList channels;

        if (null == MassageClientExecutor.ME.getChannel().get(uniqueId)) {
            channels = new CopyOnWriteArrayList<>();
        }else{
            channels = (CopyOnWriteArrayList) MassageClientExecutor.ME.getChannel().get(uniqueId);
        }

        String address = buildAdress(path);

        if(!AddressCache.getCache().contains(address)){
            KearpcSerializeProtocol protocol = KearpcSerializeProtocol.valueOf(buildProtocal(path));
            CountDownLatch countDownLatch = new CountDownLatch(1);
            MessageClientInfo clientInfo = new MessageClientInfo(
                    new InetSocketAddress(ip(address), port(address)),
                    protocol,countDownLatch);
            MassageClientExecutor.ME.setClientProperties(clientInfo).start();

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        channels.add(AddressCache.getCache().get(address));

        MassageClientExecutor.ME.getChannel().put(uniqueId,channels);
    }
    public void add(String uniqueId,List<String> paths) {
        paths.stream().forEach(path -> add(uniqueId,path));
    }

    public void delete(String uniqueId,String path){
        String address = buildAdress(path);
        Channel channel = AddressCache.getCache().get(address);
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) MassageClientExecutor.ME.getChannel().get(uniqueId);
        copyOnWriteArrayList.remove(channel);
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    public void update(String uniqueId,String path){
        delete(uniqueId,path);
        add(uniqueId,path);
    }
}
