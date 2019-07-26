package com.laniakea.client;

import com.laniakea.registry.ProviderGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wb-lgc489196
 * @version AddressMannager.java, v 0.1 2019年07月26日 10:23 wb-lgc489196 Exp
 */
public class AddressMannager {

    private Map<String, ProviderGroup> urlGroup = new ConcurrentHashMap<>();


    public int getAllSize(){
        return urlGroup.size();
    }

    public void updateUrl(String url,ProviderGroup providerGroup) {
        urlGroup.put(url,providerGroup);
    }

    public void removeUrl(String url) {
        urlGroup.remove(url);
    }


    public void updateAllUrl(Map<String, ProviderGroup> urlGroup) {
        urlGroup.putAll(urlGroup);
    }

}
