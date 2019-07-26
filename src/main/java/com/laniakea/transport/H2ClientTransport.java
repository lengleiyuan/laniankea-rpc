package com.laniakea.transport;

import com.laniakea.core.LaniakeaRequest;
import com.laniakea.core.LaniakeaResponse;
import com.laniakea.exection.KearpcException;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wb-lgc489196
 * @version H2ClientTransport.java, v 0.1 2019年07月18日 10:45 wb-lgc489196 Exp
 */
public  class H2ClientTransport extends ClientTransport {


    /**
     * Start from 3 (because 1 is setting stream)
     */
    private final static int           START_STREAM_ID = 3;
    /**
     * StreamId, start from 3 (because 1 is setting stream)
     */
    protected final      AtomicInteger streamId        = new AtomicInteger();
    /**
     * 正在发送的调用数量
     */
    protected volatile   AtomicInteger currentSize = new AtomicInteger(0);

    /**
     * Channel
     */
    protected Channel              channel;


    @Override
    public void connect() {
      /*  EventLoopGroup workerGroup = MassageClientExecutor.ME.getEventLoopGroup();
        Http2ClientInitializer initializer = new Http2ClientInitializer(transportConfig);
        try {
            String host = providerInfo.getHost();
            int port = providerInfo.getPort();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(transportConfig.isUseEpoll() ? EpollSocketChannel.class : NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.remoteAddress(host, port);
            b.handler(initializer);

            // Start the client.
            Channel channel = b.connect().syncUninterruptibly().channel();
            this.channel = channel;

            // Wait for the HTTP/2 upgrade to occur.
            Http2SettingsHandler http2SettingsHandler = initializer.settingsHandler();
            http2SettingsHandler.awaitSettings(transportConfig.getConnectTimeout(), TimeUnit.MILLISECONDS);

            responseChannelHandler = initializer.responseHandler();
            // RESET streamId
            streamId.set(START_STREAM_ID);
        } catch (Exception e) {
            //
        }*/

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void setChannel(Channel channel) {

    }

    @Override
    public Channel getChannel() {
        return null;
    }

    @Override
    public int currentSize() {
        return 0;
    }

    @Override
    public LaniakeaResponse asyncSend(LaniakeaRequest message, int timeout) throws KearpcException {
        return null;
    }

    @Override
    public LaniakeaResponse syncSend(LaniakeaRequest message, int timeout) throws KearpcException {
        return null;
    }

    @Override
    public void oneWaySend(LaniakeaRequest message, int timeout) throws KearpcException {

    }
}
