
package com.laniakea.serialize.protostuff;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class ProtostuffSerializeFactory extends BasePooledObjectFactory<ProtostuffSerialize> {

    @Override
    public ProtostuffSerialize create() throws Exception {
        return createProtostuff();
    }

    @Override
    public PooledObject<ProtostuffSerialize> wrap(ProtostuffSerialize protostuff) {
        return new DefaultPooledObject<ProtostuffSerialize>(protostuff);
    }

    private ProtostuffSerialize createProtostuff() {
        return new ProtostuffSerialize();
    }
}
