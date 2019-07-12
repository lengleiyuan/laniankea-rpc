package com.laniakea.executor;

import com.laniakea.core.Switch;
import com.laniakea.parallel.KearpcThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

import static com.laniakea.config.KearpcConstants.DEFAULT_SIZE;

/**
 * @author luochang
 * @version AbtractMassgeExecutor.java, v 0.1 2019年05月30日 11:52 luochang Exp
 */
public abstract class AbtractMassgeExecutor implements Switch {

    protected final static int parallel = Runtime.getRuntime().availableProcessors() * 2;

    protected final static int threads = 16;

    protected final static int socketSndbufSize = DEFAULT_SIZE;

    protected final static int socketRcvbufSize = DEFAULT_SIZE;

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
