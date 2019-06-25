package com.laniakea.executor;

import com.laniakea.config.KearpcProperties;
import com.laniakea.core.MessageServerHandler;
import com.laniakea.core.SerializeChannelInitializer;
import com.laniakea.parallel.NamedThreadFactory;
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

    EventLoopGroup boss = new NioEventLoopGroup();

    EventLoopGroup worker = new NioEventLoopGroup(parallel, new NamedThreadFactory(THREAD_NAME), SelectorProvider.provider());

    public volatile static MassageServerExecutor ME = MassageServerExecutor.getInstance();

    private KearpcProperties properties;

    public MassageServerExecutor setProperties(KearpcProperties properties) {
        this.properties = properties;
        return this;
    }

    public static MassageServerExecutor getInstance() {
        if (null == ME) {
            synchronized (MassageServerExecutor.class) {
                if (null == ME) {
                    ME = new MassageServerExecutor();
                }
            }
        }
        return ME;
    }

    private void logger(Void v) {
        if (logger.isInfoEnabled()) {
            logger.info("\n Server start success!\n ip: {} \n port: {} \n protocol: {} \n", properties.getIp(),
                    properties.getPort(), properties.getProtocol());
        }
    }

    public void start() {
        CompletableFuture.runAsync(new ServerInitializeTask(properties), threadPoolExecutor).thenAccept(this::logger);
    }

    public void close() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }


    class ServerInitializeTask implements Runnable {

        private KearpcProperties properties;

        public ServerInitializeTask (KearpcProperties properties) {
            this.properties = properties;
        }

        public void run() {

            try {
                ServerBootstrap b = new ServerBootstrap();

                b.group(boss, worker).channel(NioServerSocketChannel.class);

                b.option(ChannelOption.SO_BACKLOG, 1024);

                b.childOption(ChannelOption.SO_KEEPALIVE, true);

                b.handler(new LoggingHandler(LogLevel.INFO));

                b.childHandler(new SerializeChannelInitializer()
                        .buildSerialize(properties.getProtocol())
                        .buildHandle(new MessageServerHandler()));

                ChannelFuture channelFuture = b.bind(properties.getIp(), properties.getPort()).sync();

                channelFuture.addListener((listener) -> channelFuture.addListener((ChannelFuture cx) -> {
                    if (cx.isSuccess()) {
                        if (logger.isInfoEnabled()) {
                            logger.info("channel link successful");
                        }
                    } else {
                        if (logger.isInfoEnabled()) {
                            logger.info("channel is down,start to reconnecting to:" + properties.getIp() + ':'
                                    + properties.getPort());
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
