
package com.laniakea.serialize.kryo;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.laniakea.serialize.MessageCodecKit;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.common.io.Closer;

public class KryoCodecKit implements MessageCodecKit {

    private KryoPool pool;
    private  Closer closer = Closer.create();

    public KryoCodecKit(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            KryoSerialize kryoSerialization = new KryoSerialize(pool);
            kryoSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize kryoSerialization = new KryoSerialize(pool);
            Object obj = kryoSerialization.deserialize(byteArrayInputStream);
            return obj;
        } finally {
            closer.close();
        }
    }
}
