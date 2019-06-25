package com.laniakea.parallel;

import java.util.concurrent.*;

/**
 * @author luochang
 * @version KearpcThreadPool.java, v 0.1 2019年05月30日 12:01 luochang Exp
 */
public class KearpcThreadPool {

    private static final String name = "KearpcThreadPool";

    public static Executor getExecutor(int threads) {

        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new NamedThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}
