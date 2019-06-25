package com.laniakea.config;

import org.springframework.util.StringUtils;

/**
 * @author luochang
 * @version KearpcConstants.java, v 0.1 2019年06月04日 09:40 luochang Exp
 */
public interface KearpcConstants {

    String INTERFACENAME = "interfaceName";

    String PROTOCOL = "protocol";

    String DEFUALT_IP = "127.0.0.1";

    String DEFUALT_PORT = "18888";

    String DEFUALT_IP_NAME = "com.laniakea.rpc.ip";

    String DEFUALT_PORT_NAME = "com.laniakea.rpc.port";

    String PACKGE_INTERFACENAME = "value";

    String THREAD_NAME = "KearpcThreadFactory";

    String JDKSERIALIZE        = "JDKSERIALIZE";

    String KRYOSERIALIZE       = "KRYOSERIALIZE";

    String HESSIANSERIALIZE    = "HESSIANSERIALIZE";

    String PROTOSTUFFSERIALIZE = "PROTOSTUFFSERIALIZE";

    String APPLICATION_PROPERTIES ="application.properties";

    String PROXYPROPERTIES = "proxyProperties";


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
}
