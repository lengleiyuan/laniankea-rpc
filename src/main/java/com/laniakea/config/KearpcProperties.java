package com.laniakea.config;

import com.laniakea.kit.LaniakeaKit;
import com.laniakea.serialize.KearpcSerializeProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetSocketAddress;

/**
 * @author luochang
 * @version KearpcServerProperties.java, v 0.1 2019年05月29日 15:12 luochang Exp
 */

@ConfigurationProperties("com.laniakea.rpc")
public class KearpcProperties {

    private String serverAddress;

    private String registryAddress;

    private boolean isRegistry = true;

    private InetSocketAddress socketAddress = LaniakeaKit.bulidSocketAddress("0.0.0.0", 12200);

    private KearpcSerializeProtocol protocol = KearpcSerializeProtocol.PROTOSTUFFSERIALIZE;

    public KearpcSerializeProtocol getProtocol() {
        return protocol;
    }

    public boolean isRegistry() {
        return isRegistry;
    }

    public void setRegistry(boolean registry) {
        isRegistry = registry;
    }

    public String getServerAddress() {
        if(serverAddress == null){
            serverAddress = socketAddress.getHostName() + ":" + socketAddress.getPort();
        }
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setProtocol(KearpcSerializeProtocol protocol) {
        this.protocol = protocol;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
