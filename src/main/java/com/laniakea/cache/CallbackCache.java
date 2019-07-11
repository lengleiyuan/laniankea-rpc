package com.laniakea.cache;

import com.laniakea.core.MessageCallBack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luochang
 * @version HandleCache.java, v 0.1 2019年06月20日 10:19 luochang Exp
 */
public class CallbackCache {

    private Map<String, MessageCallBack> cache = new ConcurrentHashMap<>();

    private static class CacheHolder {
        public static CallbackCache cache = new CallbackCache();
    }

    public static CallbackCache getCache() {
        return CacheHolder.cache;
    }

    public void removeCallBack(String key) {
        cache.remove(key);
    }

    public void putMassgeCallBack(String key, MessageCallBack callBack) {
        cache.put(key, callBack);
    }

    public MessageCallBack getMassgeCallBack(String key) {
        return cache.get(key);
    }


}
