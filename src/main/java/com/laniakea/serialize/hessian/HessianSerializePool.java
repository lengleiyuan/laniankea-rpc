
package com.laniakea.serialize.hessian;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HessianSerializePool {

    private                 GenericObjectPool<HessianSerialize> hessianPool;
    private static volatile HessianSerializePool                poolFactory                                   = getHessianPoolInstance();
    private                 Logger                              logger                                        = LoggerFactory.getLogger( getClass());
    public static final     int                                 SERIALIZE_POOL_MAX_TOTAL                      = 500;
    public static final     int                                 SERIALIZE_POOL_MIN_IDLE                       = 10;
    public static final     int                                 SERIALIZE_POOL_MAX_WAIT_MILLIS                = 5000;
    public static final     int                                 SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000;

    private HessianSerializePool() {
        hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
    }

    public static HessianSerializePool getHessianPoolInstance() {
        if (null == poolFactory) {
            synchronized (HessianSerializePool.class) {
                if (null == poolFactory) {
                    poolFactory = new HessianSerializePool(SERIALIZE_POOL_MAX_TOTAL, SERIALIZE_POOL_MIN_IDLE,
                            SERIALIZE_POOL_MAX_WAIT_MILLIS, SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
                }
            }
        }
        return poolFactory;
    }

    public HessianSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        hessianPool.setConfig(config);
    }

    public HessianSerialize borrow() {
        try {
            return getHessianPool().borrowObject();
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    public void restore(final HessianSerialize object) {
        getHessianPool().returnObject(object);
    }

    public GenericObjectPool<HessianSerialize> getHessianPool() {
        return hessianPool;
    }
}
