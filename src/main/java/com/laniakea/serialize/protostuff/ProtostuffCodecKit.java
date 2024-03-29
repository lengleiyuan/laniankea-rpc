
package com.laniakea.serialize.protostuff;

import com.google.common.io.Closer;
import com.laniakea.serialize.MessageCodecKit;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtostuffCodecKit implements MessageCodecKit {

    private Closer closer = Closer.create();

    private ProtostuffSerializePool pool = ProtostuffSerializePool.getProtostuffPoolInstance();

    private Class<?> genericClass;

    public void setGenericClass(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            ProtostuffSerialize protostuffSerialization = pool.borrow();
            protostuffSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            pool.restore(protostuffSerialization);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            ProtostuffSerialize protostuffSerialization = pool.borrow();
            protostuffSerialization.setGenericClass(genericClass);
            Object obj = protostuffSerialization.deserialize(byteArrayInputStream);
            pool.restore(protostuffSerialization);
            return obj;
        } finally {
            closer.close();
        }
    }
}

