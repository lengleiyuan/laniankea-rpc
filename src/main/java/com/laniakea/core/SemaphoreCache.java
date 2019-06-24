package com.laniakea.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author wb-lgc489196
 * @version SemaphoreCache.java, v 0.1 2019年06月21日 14:31 wb-lgc489196 Exp
 */
public class SemaphoreCache {

    private static final Logger logger = LoggerFactory.getLogger(SemaphoreCache.class);

    private Map<String, Semaphore> cache = new ConcurrentHashMap<>();

    private static class SemaphoreCacheHolder {
        public static SemaphoreCache cache = new SemaphoreCache();
    }

    public static SemaphoreCache getCache() {
        return SemaphoreCacheHolder.cache;
    }

    public void init(String key) {
        cache.put(key, new Semaphore(0));
    }

    public void release(String key) {
        cache.get(key).release();
    }

    public void acquire(String key) {
        try {
            cache.get(key).acquire();
        } catch (InterruptedException ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
}
