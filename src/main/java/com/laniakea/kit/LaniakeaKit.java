package com.laniakea.kit;

import com.laniakea.exection.KearpcException;
import org.springframework.util.StringUtils;
import java.net.InetSocketAddress;

import static com.laniakea.config.KearpcConstants.CUTTER;
import static com.laniakea.config.KearpcConstants.ETC;

/**
 * @author wb-lgc489196
 * @version LaniakeaKit.java, v 0.1 2019年07月04日 11:03 wb-lgc489196 Exp
 */
public interface LaniakeaKit  {


    static String simpleClassName(String className){
        if(className.contains(".")){
            String[] classNameArray = className.split("\\.");
            String simpleClassNameUp = classNameArray[classNameArray.length - 1];
            return StringUtils.uncapitalize(simpleClassNameUp);
        }
        return className;
    }


    static String defaulscan(String fqClassName){
        int lastDotIndex = fqClassName.indexOf(".");
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex).concat(".").concat("**") : "");
    }

    static String ip(String address){
        String[] addressArray = address.split("\\:");
        if(addressArray.length != 2){
            try {
                throw new KearpcException("Please fill in the correct address.");
            } catch (KearpcException e) {
                e.printStackTrace();
            }
        }
        return addressArray[0];
    }


    static String buildProviderPath(String uniqueId) {
        return "/laniakea-rpc/" + uniqueId + "/providers";
    }

    static String buildConsumerPath( String uniqueId) {
        return "/laniakea-rpc/" + uniqueId + "/consumers";
    }

    static String buildConfigPath( String uniqueId) {
        return "/laniakea-rpc/" + uniqueId + "/configs";
    }

    static int port(String address) {
        String[] addressArray = address.split("\\:");
        if(addressArray.length != 2){
            try {
                throw new KearpcException("Please fill in the correct address.");
            } catch (KearpcException e) {
                e.printStackTrace();
            }
        }
        return Integer.valueOf(addressArray[1]);
    }

    static InetSocketAddress bulidSocketAddress(String host, int port){
        if (NetKit.isLocalHost(host) || NetKit.isAnyHost(host)) {
            host = SystemKit.getLocalHost();
        }
        port = NetKit.getAvailablePort(host, port);
        return new InetSocketAddress(host,port);
    }

    static String buildAdress(String path){
        return path.substring(0, path.indexOf(ETC));
    }

    static String buildProtocal(String path){
        return path.substring(path.lastIndexOf(ETC) + 1);
    }

    static String buildEndCutter(String path){
        return path.substring(path.lastIndexOf(CUTTER) + 1);
    }

    static   String hostport(String host,int port){
        return host + ":" + port;
    }

}
