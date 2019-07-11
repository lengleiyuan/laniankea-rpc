package com.laniakea.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luochang
 * @version MessageCache.java, v 0.1 2019年07月10日 10:51 luochang Exp
 */
public class MessageCache {

    private Map<String, Object>    cache = new ConcurrentHashMap<>();

    private static class MessageCacheHolder {
        public static MessageCache cache = new MessageCache();
    }

    public static MessageCache getCache() {
        return MessageCacheHolder.cache;
    }

    public void put(String key,Object value){
        cache.put(key,value);
    }

    public Object get(String key){
        return cache.get(key);
    }

    public void clear(){
        cache.clear();
    }
}
