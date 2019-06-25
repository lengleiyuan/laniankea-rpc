package com.laniakea.executor;

import com.laniakea.parallel.KearpcThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author luochang
 * @version AbtractMassgeExecutor.java, v 0.1 2019年05月30日 11:52 luochang Exp
 */
public abstract class AbtractMassgeExecutor implements MassageExecutor {

    protected final static int parallel = Runtime.getRuntime().availableProcessors() * 2;

    protected final static int threads = 16;

    public volatile static ThreadPoolExecutor threadPoolExecutor = instancePool();

    private static ThreadPoolExecutor instancePool(){
        if (null == threadPoolExecutor) {
            synchronized (AbtractMassgeExecutor.class) {
                if (null == threadPoolExecutor) {
                    threadPoolExecutor = (ThreadPoolExecutor) KearpcThreadPool.getExecutor(threads);
                }
            }
        }
        return threadPoolExecutor;
    }




}
