package com.laniakea.core;
import com.laniakea.cache.MessageCache;
import com.laniakea.cache.ReferenceCache;
import com.laniakea.cache.ServiceCache;
import com.laniakea.config.KearpcProperties;
import com.laniakea.executor.MassageClientExecutor;
import com.laniakea.executor.MassageServerExecutor;

/**
 * @author luochang
 * @version RpcRegistryInitialize.java, v 0.1 2019年05月30日 18:46 luochang Exp
 */
public class BrokerContainer implements Switch {

    private KearpcProperties properties;


    @Override
    public void start() {

        if (ServiceCache.getCache().size() > 0) {
            MassageServerExecutor.init(properties).start();
        }

    }

    @Override
    public void destroy()  {
        if(null != MassageServerExecutor.ME){
            MassageServerExecutor.ME.destroy();
        }
        MassageClientExecutor.ME.destroy();
        MessageCache.getCache().clear();
        ReferenceCache.getCache().clear();
        ServiceCache.getCache().clear();
    }


    public BrokerContainer init(KearpcProperties properties)  {
        this.properties = properties;
        return this;
    }
}
