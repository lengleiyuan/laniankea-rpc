package com.laniakea.executor;

import com.laniakea.config.ClientProxy;
import com.laniakea.controller.PullController;
import com.laniakea.core.*;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author luochang
 * @version MassgeClientExecutor.java, v 0.1 2019年05月30日 11:03 luochang Exp
 */
public class MassageClientExecutor extends AbtractMassgeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MassageClientExecutor.class);

    public static volatile MassageClientExecutor ME = MassageClientExecutor.getInstance();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);

    private ClientProxy proxy;

    public  MassageClientExecutor setClientProperties(ClientProxy proxy) {
        this.proxy = proxy;
        return this;
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
        String key = rpcInterface.getName();
        SemaphoreCache.getCache().acquire(key);
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(),
                new Class<?>[] {rpcInterface},
                new PullController<T>(MessageCache.getCache().getMassgeChannel(key)));
    }

    private void logger(Void v) {
        if (logger.isInfoEnabled()) {
            logger.info("\n Client start success!\n ip: {} \n port: {} \n protocol: {} \n", proxy.getIp(),
                    proxy.getPort(), proxy.getProtocol());
        }
    }


    public void start() {

        CompletableFuture.runAsync(new ClientInitializeTask(proxy),MassageClientExecutor.ME.threadPoolExecutor)
                .thenAccept(this::logger);

    }

    public void close() {
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    class ClientInitializeTask implements Runnable {

        private ClientProxy proxy;

        public  ClientInitializeTask (ClientProxy proxy) {
            this.proxy = proxy;
        }

        public void run() {

            Bootstrap b = new Bootstrap();

            b.group(eventLoopGroup).channel(NioSocketChannel.class);

            b.option(ChannelOption.SO_KEEPALIVE, true);

            b.handler(new SerializeChannelInitializer()
                    .buildSerialize(KearpcSerializeProtocol.valueOf(proxy.getProtocol()))
                    .buildHandle(new MessageClientHandler()));

            InetSocketAddress socketAddress = new InetSocketAddress(proxy.getIp(), proxy.getPort());

            ChannelFuture channelFuture = b.connect(socketAddress);

            channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {
                if (cx.isSuccess()) {
                    String key = proxy.getLocalInterfaceName();
                    if (logger.isInfoEnabled()){
                        logger.info("channel link successful key: " .concat(key));
                    }
                    MessageCache.getCache().putMassgeChannel(key,cx.channel());
                    SemaphoreCache.getCache().release(key);
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
