package com.laniakea.config;


/**
 * @author luochang
 * @version ClientProperties.java, v 0.1 2019年05月30日 18:27 luochang Exp
 */
public class ClientProxy {

    private String remoteInterfaceName;

    private String localInterfaceName;

    private String ip;

    private Integer port;

    private String protocol;

    public ClientProxy(String remoteInterfaceName, String localInterfaceName, String ip, Integer port, String protocol){
        this.remoteInterfaceName = remoteInterfaceName;
        this.localInterfaceName = localInterfaceName;
        this.ip = ip;
        this.port = port;
        this.protocol = protocol;
    }

    public String getLocalInterfaceName() {
        return localInterfaceName;
    }

    public void setLocalInterfaceName(String localInterfaceName) {
        this.localInterfaceName = localInterfaceName;
    }

    public String getRemoteInterfaceName() {
        return remoteInterfaceName;
    }

    public void setRemoteInterfaceName(String remoteInterfaceName) {
        this.remoteInterfaceName = remoteInterfaceName;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
