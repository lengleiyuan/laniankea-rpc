package com.laniakea.client.lb;

import com.laniakea.client.AbstractLoadBalancer;
import com.laniakea.core.ConsumerConfig;
import com.laniakea.core.LaniakeaRequest;
import com.laniakea.registry.Provider;
import com.laniakea.registry.ProviderGroup;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author wb-lgc489196
 * @version RandomLoadBalancer.java, v 0.1 2019年07月26日 14:35 wb-lgc489196 Exp
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    private final Random random = new Random();

    public RandomLoadBalancer(ConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    public Provider doSelectUrl(LaniakeaRequest request, ProviderGroup providerGroup) {

        List<Provider> providers = providerGroup.getProviders();

        Collections.shuffle(providers);

        int size = providers.size();

        int index  = random.nextInt(size);

        return providers.get(index);
    }
}
