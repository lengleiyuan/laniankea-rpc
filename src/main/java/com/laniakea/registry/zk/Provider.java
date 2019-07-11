package com.laniakea.registry.zk;

import com.laniakea.serialize.KearpcSerializeProtocol;

/**
 * @author wb-lgc489196
 * @version Provider.java, v 0.1 2019年07月04日 10:42 wb-lgc489196 Exp
 */
public class Provider {

    private String host;

    private int port;

    private String uniqueId;

    private KearpcSerializeProtocol protocol;

    public Provider(String host,int port){
        this.host = host;
        this.port = port;
    }
    
    public Provider(String host,int port,String uniqueId,KearpcSerializeProtocol protocol){
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.uniqueId = uniqueId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public KearpcSerializeProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(KearpcSerializeProtocol protocol) {
        this.protocol = protocol;
    }
}
