package com.laniakea.executor;

import com.laniakea.annotation.KearpcReference;
import com.laniakea.cache.AddressCache;
import com.laniakea.controller.AsyncPullController;
import com.laniakea.core.*;
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
import java.util.concurrent.*;

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

    private MessageClientInfo clientInfo;

    private Map<String, CopyOnWriteArrayList<Channel>> cache = new ConcurrentHashMap<>();

    public  MassageClientExecutor setClientProperties(MessageClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        return this;
    }

    public CopyOnWriteArrayList<Channel> get(String key){
        return cache.get(key);
    }

    public  Map<String, CopyOnWriteArrayList<Channel>> getChannel(){
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

        CopyOnWriteArrayList<Channel> channels = cache.get(rpcInterface.getName());
        KearpcReference reference = rpcInterface.getAnnotation(KearpcReference.class);
        int concurrentSize = reference.maximumSize();
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(),
                new Class[] {rpcInterface},
                new MessageEventAdvice(controller,
                new BalanceMessageProxyInterceptor<T>(channels,new Semaphore(concurrentSize))));
    }



    public void start() {

        CompletableFuture.runAsync(new ClientInitializeTask(clientInfo));

    }

    public void close () {
        cache.values().forEach(channels ->channels.forEach(
                channel -> {
                    try {
                        channel.close().sync();
                    } catch (InterruptedException e) {
                        logger.warn(e.getMessage(),e);
                    }
             }));
        eventLoopGroup.shutdownGracefully();
        cache.clear();

    }

    class ClientInitializeTask implements Runnable {

        private final MessageClientInfo clientInfo;


        public  ClientInitializeTask (MessageClientInfo clientInfo) {
            this.clientInfo = clientInfo;
        }

        public void run() {

            bootstrap = new Bootstrap();

            eventLoopGroup =  new NioEventLoopGroup(parallel);

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_SNDBUF, socketSndbufSize);

            bootstrap.option(ChannelOption.SO_RCVBUF, socketRcvbufSize);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            bootstrap.handler(new SerializeChannelInitializer()
                    .buildSerialize(clientInfo.getSerialize())
                    .buildHandle(new MessageClientHandler()));


            ChannelFuture channelFuture = bootstrap.connect(clientInfo.getSocketAddress());

            channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {
                InetSocketAddress socketAddress = clientInfo.getSocketAddress();
                if (cx.isSuccess()) {
                    if (logger.isInfoEnabled()) {
                        logger.info("channel link successful, serialize: {} ", clientInfo.getSerialize());
                    }
                    AddressCache.getCache().put(hostport(socketAddress.getHostName(), socketAddress.getPort()), cx.channel());
                    clientInfo.getCoundDownLatch().countDown();
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("channel is down,start to reconnecting to: " + socketAddress.getHostName() + ':'
                                + socketAddress.getPort());
                    }
                    eventLoopGroup.schedule(this, 10, TimeUnit.SECONDS);
                }
            }));

        }
    }

}
