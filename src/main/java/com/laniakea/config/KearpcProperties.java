package com.laniakea.config;

import com.laniakea.serialize.KearpcSerializeProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author luochang
 * @version KearpcServerProperties.java, v 0.1 2019年05月29日 15:12 luochang Exp
 */

@ConfigurationProperties("com.laniakea.rpc")
public class KearpcProperties {

    private String address;

    private KearpcSerializeProtocol protocol;

    public KearpcSerializeProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(KearpcSerializeProtocol protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
