package com.laniakea.config;

import com.laniakea.serialize.KearpcSerializeProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author luochang
 * @version KearpcServerProperties.java, v 0.1 2019年05月29日 15:12 luochang Exp
 */

@ConfigurationProperties("com.laniakea.rpc")
public class KearpcProperties {

    private String ip;

    private Integer port;

    private KearpcSerializeProtocol protocol;

    public KearpcSerializeProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(KearpcSerializeProtocol protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
