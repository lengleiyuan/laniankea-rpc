package com.laniakea.spring;
import com.laniakea.config.KearpcProperties;
import com.laniakea.executor.MassageServerExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author luochang
 * @version RpcRegistryInitialize.java, v 0.1 2019年05月30日 18:46 luochang Exp
 */
public class RegistryInitialize implements InitializingBean, DisposableBean {


    private KearpcProperties properties;

    public void setKearpcProperties(KearpcProperties properties) {
        this.properties = properties;
    }


    @Override
    public void destroy()  {
        MassageServerExecutor.ME.close();
    }

    @Override
    public void afterPropertiesSet()  {
        MassageServerExecutor.init(properties).start();
    }
}
