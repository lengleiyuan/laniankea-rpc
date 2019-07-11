package com.laniakea.cache;

import com.laniakea.annotation.KearpcService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luochang
 * @version ServiceCache.java, v 0.1 2019年07月09日 10:40 luochang Exp
 */
public class ServiceCache {

    private Map<String, KearpcService> cache = new HashMap<>();

    private static class ServiceCacheHolder {
        public static ServiceCache cache = new ServiceCache();
    }

    public static ServiceCache getCache() {
        return ServiceCacheHolder.cache;
    }

    public  Map<String,KearpcService> get(){
        return cache;
    }

    public void clear(){
        cache.clear();
    }
}
