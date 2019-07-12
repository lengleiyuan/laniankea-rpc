package com.laniakea.executor;

import com.laniakea.config.KearpcProperties;
import com.laniakea.core.MessageServerHandler;
import com.laniakea.core.SerializeChannelInitializer;
import com.laniakea.kit.LaniakeaKit;
import com.laniakea.parallel.NamedThreadFactory;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.laniakea.config.KearpcConstants.THREAD_NAME;

/**
 * @author luochang
 * @version kearpcServerExecutor.java, v 0.1 2019年05月29日 16:06 luochang Exp
 */
public class MassageServerExecutor extends AbtractMassgeExecutor {

    private static Logger logger = LoggerFactory.getLogger(MassageServerExecutor.class);

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    private ServerBootstrap bootstrap;

    public volatile static MassageServerExecutor ME;

    private final String ip;

    private final Integer port;

    private final KearpcSerializeProtocol protocol;


    public MassageServerExecutor(final KearpcProperties properties){
        this.ip = LaniakeaKit.ip(properties.getServerAddress());
        this.port = Integer.valueOf(LaniakeaKit.port(properties.getServerAddress()));
        this.protocol = properties.getProtocol();
    }

    public static MassageServerExecutor init(KearpcProperties properties) {
        if (null == ME) {
            synchronized (MassageServerExecutor.class) {
                if (null == ME) {
                    ME = new MassageServerExecutor(properties);
                }
            }
        }
        return ME;
    }


    public void start() {
        CompletableFuture.runAsync(new ServerInitializeTask());
    }


    public void close() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
        MassageClientExecutor.threadPoolExecutor.shutdown();
    }


    class ServerInitializeTask implements Runnable {


        public void run() {

            try {
                bootstrap = new ServerBootstrap();

                boss = new NioEventLoopGroup();

                worker =  new NioEventLoopGroup(parallel, new NamedThreadFactory(THREAD_NAME), SelectorProvider.provider());

                bootstrap.group(boss, worker).channel(NioServerSocketChannel.class);

                bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

                bootstrap.option(ChannelOption.SO_REUSEADDR, true);

                bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

                bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

                bootstrap.option(ChannelOption.SO_SNDBUF, socketSndbufSize);

                bootstrap.option(ChannelOption.SO_RCVBUF, socketRcvbufSize);

                bootstrap.handler(new LoggingHandler(LogLevel.INFO));

                bootstrap.childHandler(new SerializeChannelInitializer()
                        .buildSerialize(protocol)
                        .buildHandle(new MessageServerHandler()));

                ChannelFuture channelFuture = bootstrap.bind(ip, port).sync();

                channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {
                    if (cx.isSuccess()) {
                        if (logger.isInfoEnabled()) {
                            logger.info("channel link successful, serialize: {} ",protocol);
                        }
                    } else {
                        if (logger.isInfoEnabled()) {
                            logger.info("channel is down,start to reconnecting to:" + ip + ':'
                                    + port);
                        }
                        boss.schedule(this, 10, TimeUnit.SECONDS);
                    }
                }));
            } catch (InterruptedException e) {
                logger.warn(e.getMessage(),e);
            }
        }
    }

}
