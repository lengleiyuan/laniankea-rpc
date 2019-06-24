/**
 * Copyright (C) 2016 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.laniakea.serialize.protostuff;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtostuffSerializePool {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private                 GenericObjectPool<ProtostuffSerialize> protostuffPool;
    private static volatile ProtostuffSerializePool                poolFactory = null;

    private ProtostuffSerializePool() {
        protostuffPool = new GenericObjectPool<ProtostuffSerialize>(new ProtostuffSerializeFactory());
    }

    public static final int SERIALIZE_POOL_MAX_TOTAL                      = 500;
    public static final int SERIALIZE_POOL_MIN_IDLE                       = 10;
    public static final int SERIALIZE_POOL_MAX_WAIT_MILLIS                = 5000;
    public static final int SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000;

    public static ProtostuffSerializePool getProtostuffPoolInstance() {
        if (poolFactory == null) {
            synchronized (ProtostuffSerializePool.class) {
                if (poolFactory == null) {
                    poolFactory = new ProtostuffSerializePool(SERIALIZE_POOL_MAX_TOTAL, SERIALIZE_POOL_MIN_IDLE,
                            SERIALIZE_POOL_MAX_WAIT_MILLIS, SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
                }
            }
        }
        return poolFactory;
    }

    public ProtostuffSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        protostuffPool = new GenericObjectPool<ProtostuffSerialize>(new ProtostuffSerializeFactory());

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        protostuffPool.setConfig(config);
    }

    public ProtostuffSerialize borrow() {
        try {
            return getProtostuffPool().borrowObject();
        } catch (final Exception ex) {
            logger.error(ex.getMessage(),ex);
            return null;
        }
    }

    public void restore(final ProtostuffSerialize object) {
        getProtostuffPool().returnObject(object);
    }

    public GenericObjectPool<ProtostuffSerialize> getProtostuffPool() {
        return protostuffPool;
    }
}

