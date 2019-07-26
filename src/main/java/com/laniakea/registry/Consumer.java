package com.laniakea.registry;

import com.laniakea.kit.LaniakeaKit;

import java.net.InetSocketAddress;

/**
 * @author luochang
 * @version Consumer.java, v 0.1 2019年07月04日 15:21 luochang Exp
 */
public class Consumer {

    private String remoteKey;

    private String nativeKey;

    private InetSocketAddress socketAddress =  LaniakeaKit.bulidSocketAddress("0.0.0.0", 12200);

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public String getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    public String getNativeKey() {
        return nativeKey;
    }

    public void setNativeKey(String nativeKey) {
        this.nativeKey = nativeKey;
    }
}
