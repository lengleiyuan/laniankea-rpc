package com.laniakea.spring;

import com.laniakea.config.ClientProxy;
import com.laniakea.exection.KearpcException;
import com.laniakea.executor.MassageClientExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author luochang
 * @version ReferenceInitializeFactory.java, v 0.1 2019年05月30日 17:56 luochang Exp
 */
public class ReferenceInitializeFactory implements FactoryBean, InitializingBean, DisposableBean {


    private ClientProxy proxyProperties;

    @Override
    public void destroy()  {
        MassageClientExecutor.ME.close();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() {
        return MassageClientExecutor.ME.execute(getObjectType());
    }

    @Override
    public Class<?> getObjectType() {
        if(null == proxyProperties){
            return null;
        }
        try {
            return this.getClass().getClassLoader().loadClass(proxyProperties.getLocalInterfaceName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    @Override
    public void afterPropertiesSet()  {
        MassageClientExecutor.ME.setClientProperties(proxyProperties).start();
    }

    public ClientProxy getProxyProperties() {
        return proxyProperties;
    }

    public void setProxyProperties(ClientProxy proxyProperties) {
        this.proxyProperties = proxyProperties;
    }
}
