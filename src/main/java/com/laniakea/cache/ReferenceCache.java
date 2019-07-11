package com.laniakea.cache;

import com.laniakea.annotation.KearpcReference;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luochang
 * @version ReferenceCache.java, v 0.1 2019年07月08日 14:27 luochang Exp
 */
public class ReferenceCache {

    private Map<String,KearpcReference> cache = new HashMap<>();

    private static class ReferenceCacheHolder {
        public static ReferenceCache cache = new ReferenceCache();
    }

    public static ReferenceCache getCache() {
        return ReferenceCacheHolder.cache;
    }

    public  Map<String,KearpcReference> get(){
        return cache;
    }

    public void clear(){
        cache.clear();
    }
}
