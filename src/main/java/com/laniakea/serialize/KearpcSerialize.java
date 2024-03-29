package com.laniakea.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author luochang
 * @version KearpcSerialize.java, v 0.1 2019年05月30日 13:43 luochang Exp
 */
public interface KearpcSerialize {

    void serialize(OutputStream output, Object object) throws IOException;

    Object deserialize(InputStream input) throws IOException;
}
