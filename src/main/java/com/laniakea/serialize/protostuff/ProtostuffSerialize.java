
package com.laniakea.serialize.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import java.io.InputStream;
import java.io.OutputStream;
import com.laniakea.serialize.KearpcSerialize;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class ProtostuffSerialize implements KearpcSerialize {

    private static SchemaCache cachedSchema = SchemaCache.getInstance();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private  Class<?> genericClass;

    public void setGenericClass(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        return (Schema<T>) cachedSchema.get(cls);
    }

    @Override
    public Object deserialize(InputStream input) {
        try {
            Object message = objenesis.newInstance(genericClass);
            Schema<Object> schema = getSchema((Class<Object>) genericClass);
            ProtostuffIOUtil.mergeFrom(input, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void serialize(OutputStream output, Object object) {
        Class cls =  object.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(output, object, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
}

