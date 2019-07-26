package com.laniakea.transport;

import com.laniakea.core.ConsumerConfig;
import com.laniakea.core.LaniakeaRequest;
import com.laniakea.core.LaniakeaResponse;
import com.laniakea.exection.KearpcException;
import io.netty.channel.Channel;

/**
 * @author wb-lgc489196
 * @version ClientTransport.java, v 0.1 2019年07月18日 10:48 wb-lgc489196 Exp
 */
public abstract class ClientTransport {

    protected ConsumerConfig clientInfo;

    public void setClientInfo(ConsumerConfig clientInfo) {
        this.clientInfo = clientInfo;
    }



    public abstract void connect();

    public abstract void disconnect();

    public abstract void destroy();

    public abstract boolean isAvailable();

    public abstract void setChannel(Channel channel);

    public abstract Channel getChannel();

    public abstract int currentSize();

    public abstract LaniakeaResponse asyncSend(LaniakeaRequest message, int timeout) throws KearpcException;

    public abstract LaniakeaResponse syncSend(LaniakeaRequest message, int timeout) throws KearpcException;

    public abstract void oneWaySend(LaniakeaRequest message, int timeout) throws KearpcException;
}
