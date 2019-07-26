package com.laniakea.spring;

import com.laniakea.executor.MassageClientExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author luochang
 * @version ReferenceInitializeFactory.java, v 0.1 2019年05月30日 17:56 luochang Exp
 */
public class ReferenceFactory implements FactoryBean, DisposableBean {


    private String innerInterfaceName;


    public String getInnerInterfaceName() {
        return innerInterfaceName;
    }

    public void setInnerInterfaceName(String innerInterfaceName) {
        this.innerInterfaceName = innerInterfaceName;
    }

    @Override
    public void destroy()  {
        MassageClientExecutor.ME.destroy();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() {
        return MassageClientExecutor.ME.create(getObjectType());
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.innerInterfaceName == null ? null : this.getClass().getClassLoader().loadClass(this.innerInterfaceName);
        } catch (ClassNotFoundException e) {
            // ignore
            return null;
        }
    }

}
