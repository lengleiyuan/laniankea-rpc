package com.laniakea.spring;

import com.laniakea.core.BrokerContainer;
import com.laniakea.registry.Registry;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author luochang
 * @version ApplicationContextClosedListener.java, v 0.1 2019年07月09日 15:46 luochang Exp
 */
public class ApplicationContextClosedListener implements ApplicationListener {

    private BrokerContainer brokerContainer;

    private Registry registry;

    public ApplicationContextClosedListener(BrokerContainer brokerContainer, Registry registry ){
        this.brokerContainer = brokerContainer;
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if ((event instanceof ContextClosedEvent) || (event instanceof ContextStoppedEvent)) {

            brokerContainer.close();

            registry.destroy();

        }
    }
}
