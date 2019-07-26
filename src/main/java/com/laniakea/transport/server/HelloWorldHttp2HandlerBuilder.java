package com.laniakea.transport.server;

/**
 * @author wb-lgc489196
 * @version HelloWorldHttp2HandlerBuilder.java, v 0.1 2019年07月18日 15:18 wb-lgc489196 Exp
 */
import io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2ConnectionDecoder;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2Settings;

import static io.netty.handler.logging.LogLevel.INFO;

public final class HelloWorldHttp2HandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<HelloWorldHttp2Handler, HelloWorldHttp2HandlerBuilder> {

    private static final Http2FrameLogger logger = new Http2FrameLogger(INFO, HelloWorldHttp2Handler.class);

    public HelloWorldHttp2HandlerBuilder() {
        frameLogger(logger);
    }

    @Override
    public HelloWorldHttp2Handler build() {
        return super.build();
    }

    @Override
    protected HelloWorldHttp2Handler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder,
                                           Http2Settings initialSettings) {
        HelloWorldHttp2Handler handler = new HelloWorldHttp2Handler(decoder, encoder, initialSettings);
        frameListener(handler);
        return handler;
    }
}