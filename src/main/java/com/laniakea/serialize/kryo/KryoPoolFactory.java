
package com.laniakea.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.laniakea.core.LaniakeaRequest;
import com.laniakea.core.LaniakeaResponse;
import org.objenesis.strategy.StdInstantiatorStrategy;


public class KryoPoolFactory {

    private static volatile KryoPoolFactory poolFactory = null;

    private KryoFactory factory = ()->{
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(LaniakeaRequest.class);
        kryo.register(LaniakeaResponse.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    };

    private KryoPool pool = new KryoPool.Builder(factory).build();

    private KryoPoolFactory() {
    }

    public static KryoPool getKryoPoolInstance() {
        if (poolFactory == null) {
            synchronized (KryoPoolFactory.class) {
                if (poolFactory == null) {
                    poolFactory = new KryoPoolFactory();
                }
            }
        }
        return poolFactory.getPool();
    }

    public KryoPool getPool() {
        return pool;
    }

}

