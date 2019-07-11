package com.laniakea.registry.zk;

import com.laniakea.kit.LaniakeaKit;

import java.net.InetSocketAddress;

/**
 * @author wb-lgc489196
 * @version Consumer.java, v 0.1 2019年07月04日 15:21 wb-lgc489196 Exp
 */
public class Consumer {

    private String uniqueId;

    private InetSocketAddress socketAddress =  LaniakeaKit.bulidSocketAddress("0.0.0.0", 12200);

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
