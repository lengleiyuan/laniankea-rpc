package com.laniakea.config;

import com.laniakea.exection.KearpcException;
import org.springframework.util.StringUtils;

/**
 * @author luochang
 * @version KearpcConstants.java, v 0.1 2019年06月04日 09:40 luochang Exp
 */
public interface KearpcConstants {

    String INTERFACENAME = "interfaceName";

    String PROTOCOL = "protocol";

    String ADDRESS = "address";

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

    static String port(String address) {
        String[] addressArray = address.split("\\:");
        if(addressArray.length != 2){
            try {
                throw new KearpcException("Please fill in the correct address.");
            } catch (KearpcException e) {
                e.printStackTrace();
            }
        }
        return addressArray[1];
    }
}
