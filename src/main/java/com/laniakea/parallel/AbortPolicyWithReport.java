package com.laniakea.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wb-lgc489196
 * @version AbortPolicyWithReport.java, v 0.1 2019年05月30日 12:07 wb-lgc489196 Exp
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String threadName;

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("RpcServer["
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]", threadName, e.getPoolSize(),
                e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());

        logger.warn(msg);

        throw new RejectedExecutionException(msg);
    }
}
