package com.laniakea.executor;

import com.laniakea.cache.SemaphoreCache;
import com.laniakea.controller.AsyncPullController;
import com.laniakea.core.MessageClientHandler;
import com.laniakea.core.MessageEventAdvice;
import com.laniakea.core.SerializeChannelInitializer;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.laniakea.kit.LaniakeaKit.hostport;

/**
 * @author luochang
 * @version MassgeClientExecutor.java, v 0.1 2019年05月30日 11:03 luochang Exp
 */
public class MassageClientExecutor<T> extends AbtractMassgeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MassageClientExecutor.class);

    public static volatile MassageClientExecutor ME = MassageClientExecutor.getInstance();

    private volatile AsyncPullController<T> controller = new AsyncPullController<>();

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private InetSocketAddress socketAddress;

    private KearpcSerializeProtocol protocol;

    private Map<String, Channel> cache = new ConcurrentHashMap<>();

    public  MassageClientExecutor setClientProperties(InetSocketAddress socketAddress, KearpcSerializeProtocol protocol) {
        this.socketAddress = socketAddress;
        this.protocol = protocol;
        return this;
    }

    public Channel get(String key){
        return cache.get(key);
    }

    public  Map<String, Channel> getChannel(){
        return cache;
    }


    public static MassageClientExecutor getInstance() {
        if (null == ME) {
            synchronized (MassageClientExecutor.class) {
                if (null == ME) {
                    ME = new MassageClientExecutor();
                }
            }
        }
        return ME;
    }


    public <T> T create(Class<T> rpcInterface) {

        String address = hostport(socketAddress.getHostName(), socketAddress.getPort());
        SemaphoreCache.getCache().acquire(address);

        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] {rpcInterface},
                new MessageEventAdvice(controller,controller, address));
    }



    public void start() {

        CompletableFuture.runAsync(new ClientInitializeTask(socketAddress,protocol));

    }

    public void close() {

        cache.values().forEach(channel -> {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                logger.warn(e.getMessage(),e);
            }
        });

        eventLoopGroup.shutdownGracefully();
        cache.clear();

    }

    class ClientInitializeTask implements Runnable {

        private InetSocketAddress socketAddress;

        private KearpcSerializeProtocol protocol;


        public  ClientInitializeTask (InetSocketAddress socketAddress,KearpcSerializeProtocol protocol) {
            this.socketAddress = socketAddress;
            this.protocol = protocol;
        }

        public void run() {

            bootstrap = new Bootstrap();

            eventLoopGroup =  new NioEventLoopGroup(parallel);

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.handler(new SerializeChannelInitializer()
                    .buildSerialize(protocol)
                    .buildHandle(new MessageClientHandler()));


            ChannelFuture channelFuture = bootstrap.connect(socketAddress);

            channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {
                if (cx.isSuccess()) {
                   if (logger.isInfoEnabled()){
                        logger.info("channel link successful, serialize: {} ",protocol);
                    }
                    cache.put(hostport(socketAddress.getHostName(),socketAddress.getPort()),cx.channel());
                    SemaphoreCache.getCache().release(hostport(socketAddress.getHostName(),socketAddress.getPort()));
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("channel is down,start to reconnecting to: " + socketAddress.getAddress().getHostAddress() + ':'
                                + socketAddress.getPort());
                    }
                    eventLoopGroup.schedule(this, 10, TimeUnit.SECONDS);
                }
            }));

        }

    }

}
