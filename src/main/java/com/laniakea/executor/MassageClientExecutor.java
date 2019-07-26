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
import java.util.concurrent.atomic.AtomicInteger;

import static com.laniakea.kit.LaniakeaKit.hostport;

/**
 * @author luochang
 * @version MassgeClientExecutor.java, v 0.1 2019年05月30日 11:03 luochang Exp
 */
public class MassageClientExecutor<T> extends AbtractMassgeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MassageClientExecutor.class);

    public static volatile MassageClientExecutor ME = MassageClientExecutor.getInstance();

    private volatile AsyncPullController<T> controller = new AsyncPullController<>();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);

    private static ConcurrentMap<EventLoopGroup, AtomicInteger> refCounter = new ConcurrentHashMap<>();

    private ConsumerConfig clientInfo;

    private Map<String, CopyOnWriteArrayList<Channel>> cache = new ConcurrentHashMap<>();

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public  MassageClientExecutor setClientProperties(ConsumerConfig clientInfo) {
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

        MessageBalanceProxyInterceptor<T> interceptor = new MessageBalanceProxyInterceptor<>
                (channels, new Semaphore(reference.maximumSize()));
        MessageEventAdvice messageEventAdvice = new MessageEventAdvice(controller, interceptor);

        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(),
                new Class[] {rpcInterface}, messageEventAdvice);
    }



    public void start() {

      /*  CompletableFuture.runAsync(new ClientInitializeTask(clientInfo))
                .thenAccept((Void) -> clientInfo.getCoundDownLatch().countDown())
                .thenAccept();*/

    }

    public void destroy () {
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

        private final ConsumerConfig clientInfo;


        public  ClientInitializeTask (ConsumerConfig clientInfo) {
            this.clientInfo = clientInfo;
        }

        public void run() {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_SNDBUF, socketSndbufSize);

            bootstrap.option(ChannelOption.SO_RCVBUF, socketRcvbufSize);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            bootstrap.handler(new SerializeChannelInitializer()
                    .buildSerialize(clientInfo.getSerialize())
                    .buildHandle(new MessageClientHandler()));


            ChannelFuture channelFuture = bootstrap.connect(clientInfo.getSocketAddress());
            Channel channel = bootstrap.connect().syncUninterruptibly().channel();

            InetSocketAddress socketAddress = clientInfo.getSocketAddress();
            AddressCache.getCache().put(hostport(socketAddress.getHostName(), socketAddress.getPort()),channel);

            channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {

                if (cx.isSuccess()) {
                    if (logger.isInfoEnabled()) {
                        logger.info("channel link successful, serialize: {} ", clientInfo.getSerialize());
                    }


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
