package com.laniakea.cache;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wb-lgc489196
 * @version AddressMapCache.java, v 0.1 2019年07月12日 16:10 wb-lgc489196 Exp
 */
public class AddressCache {

    private Map<String, Channel> cache = new ConcurrentHashMap<>();

    private static class AddressMapCacheHolder {
        public static AddressCache cache = new AddressCache();
    }

    public static AddressCache getCache() {
        return AddressMapCacheHolder.cache;
    }

    public boolean contains(String key){
        return cache.containsKey(key);
    }

    public void put(String key,Channel value){
        cache.put(key,value);
    }

    public Channel get(String key){
        return cache.get(key);
    }

    public void clear(){
        cache.clear();
    }
}
