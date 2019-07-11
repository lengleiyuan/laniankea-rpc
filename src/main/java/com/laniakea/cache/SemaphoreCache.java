package com.laniakea.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author luochang
 * @version SemaphoreCache.java, v 0.1 2019年06月21日 14:31 luochang Exp
 */
public class SemaphoreCache {

    private static final Logger logger = LoggerFactory.getLogger(SemaphoreCache.class);

    private static final LoadingCache<String, Semaphore> cache = CacheBuilder.newBuilder().
            concurrencyLevel(Runtime.getRuntime().availableProcessors() * 2)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(()->new Semaphore(0)));


    private static class SemaphoreCacheHolder {
        public static SemaphoreCache cache = new SemaphoreCache();
    }

    public static SemaphoreCache getCache() {
        return SemaphoreCacheHolder.cache;
    }

    public void release(String key) {
        try {
            cache.get(key).release();
        } catch (ExecutionException ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    public void acquire(String key) {
        try {
            cache.get(key).acquire();
        } catch (InterruptedException ex) {
            logger.warn(ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    public void clear(){
        cache.cleanUp();
    }
}
