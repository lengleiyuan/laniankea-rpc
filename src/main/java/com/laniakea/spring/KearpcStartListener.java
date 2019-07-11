package com.laniakea.spring;

import com.laniakea.config.KearpcProperties;
import com.laniakea.core.BrokerContainer;
import com.laniakea.registry.Registry;
import com.laniakea.registry.RegistryContainer;
import org.springframework.context.ApplicationListener;

/**
 * @author luochang
 * @version KearpcStartListener.java, v 0.1 2019年07月09日 15:23 luochang Exp
 */
public class KearpcStartListener implements ApplicationListener<KearpcBootEvent> {

    private KearpcProperties properties;

    private Registry registry;

    private RegistryContainer registryContainer;

    private BrokerContainer brokerContainer;


    public KearpcStartListener(KearpcProperties properties,
                               Registry registry,
                               RegistryContainer registryContainer,
                               BrokerContainer brokerContainer){
        this.properties = properties;
        this.registry = registry;
        this.registryContainer = registryContainer;
        this.brokerContainer = brokerContainer;
    }

    @Override
    public void onApplicationEvent(KearpcBootEvent event) {

        brokerContainer.init(properties).start();

        registryContainer.publishCheck();

        registryContainer.publishAllConfig(properties,registry);
    }
}
