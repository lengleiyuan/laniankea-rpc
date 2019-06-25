package com.laniakea.core;

/**
 * @author luochang
 * @version MessageRequest.java, v 0.1 2019年05月30日 12:21 luochang Exp
 */
import java.io.Serializable;

public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String className;

    private String methodName;

    private Class<?>[] typeParameters;

    private Object[] parametersVal;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Object[] getParameters() {
        return parametersVal;
    }

    public void setParameters(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }

}
