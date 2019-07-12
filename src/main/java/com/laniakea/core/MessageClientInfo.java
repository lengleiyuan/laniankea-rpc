package com.laniakea.core;

import com.laniakea.serialize.KearpcSerializeProtocol;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author wb-lgc489196
 * @version ClientInfo.java, v 0.1 2019年07月11日 17:38 wb-lgc489196 Exp
 */
public class MessageClientInfo {

    private InetSocketAddress socketAddress;

    private KearpcSerializeProtocol serialize;

    private CountDownLatch coundDownLatch;


    public MessageClientInfo(){}

    public MessageClientInfo(InetSocketAddress socketAddress, KearpcSerializeProtocol serialize,CountDownLatch coundDownLatch){
        this.socketAddress = socketAddress;
        this.serialize = serialize;
        this.coundDownLatch = coundDownLatch;
    }

    public CountDownLatch getCoundDownLatch() {
        return coundDownLatch;
    }

    public void setCoundDownLatch(CountDownLatch coundDownLatch) {
        this.coundDownLatch = coundDownLatch;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public KearpcSerializeProtocol getSerialize() {
        return serialize;
    }

    public void setSerialize(KearpcSerializeProtocol serialize) {
        this.serialize = serialize;
    }

}
