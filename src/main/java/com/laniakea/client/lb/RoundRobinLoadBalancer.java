package com.laniakea.client.lb;

import com.laniakea.client.AbstractLoadBalancer;
import com.laniakea.core.ConsumerConfig;
import com.laniakea.core.LaniakeaRequest;
import com.laniakea.registry.Provider;
import com.laniakea.registry.ProviderGroup;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wb-lgc489196
 * @version RoundRobinLoadBalancer.java, v 0.1 2019年07月26日 11:00 wb-lgc489196 Exp
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    private final ConcurrentMap<String, PositiveAtomicCounter> sequences = new ConcurrentHashMap<>();

    public RoundRobinLoadBalancer(ConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    protected Provider doSelectUrl(LaniakeaRequest request, ProviderGroup providerGroup) {
        String key = request.getClassName();
        List<Provider> providers = providerGroup.getProviders();
        int length = providers.size();
        PositiveAtomicCounter sequence = sequences.get(key);
        if (sequence == null) {
            sequences.putIfAbsent(key, new PositiveAtomicCounter());
            sequence = sequences.get(key);
        }
        return providers.get(sequence.getAndIncrement() % length);
    }


    public class PositiveAtomicCounter {
        private static final int           MASK = 0x7FFFFFFF;
        private final        AtomicInteger atom;

        public PositiveAtomicCounter() {
            atom = new AtomicInteger(0);
        }

        public final int incrementAndGet() {
            return atom.incrementAndGet() & MASK;
        }

        public final int getAndIncrement() {
            return atom.getAndIncrement() & MASK;
        }

        public int get() {
            return atom.get() & MASK;
        }

    }
}
