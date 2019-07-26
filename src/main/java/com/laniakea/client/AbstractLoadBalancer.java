package com.laniakea.client;

import com.laniakea.core.ConsumerConfig;
import com.laniakea.core.LaniakeaRequest;
import com.laniakea.exection.KearpcException;
import com.laniakea.registry.Provider;
import com.laniakea.registry.ProviderGroup;

/**
 * @author wb-lgc489196
 * @version LoadBalancerMannager.java, v 0.1 2019年07月26日 10:39 wb-lgc489196 Exp
 */
public abstract class AbstractLoadBalancer {

    protected final ConsumerConfig  consumerConfig;

    public AbstractLoadBalancer(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public Provider selectUrl(LaniakeaRequest request){

        ProviderGroup providerGroup = consumerConfig.getProviderGroup();

        if(providerGroup.getProviders().size() == 0){
            throw new KearpcException("provider address is null!");
        }
        if(providerGroup.getProviders().size() == 1){
            return providerGroup.getProviders().get(0);
        }
        return doSelectUrl(request,providerGroup);
    }


    protected abstract Provider doSelectUrl(LaniakeaRequest request, ProviderGroup providerGroup);

}
