package com.laniakea.registry;

import com.laniakea.annotation.KearpcReference;
import com.laniakea.annotation.KearpcService;
import com.laniakea.cache.AddressCache;
import com.laniakea.cache.ReferenceCache;
import com.laniakea.cache.ServiceCache;
import com.laniakea.config.KearpcProperties;
import com.laniakea.core.ConsumerConfig;
import com.laniakea.executor.MassageClientExecutor;
import com.laniakea.kit.LaniakeaKit;
import com.laniakea.serialize.KearpcSerializeProtocol;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static com.laniakea.kit.LaniakeaKit.ip;
import static com.laniakea.kit.LaniakeaKit.port;

/**
 * @author luochang
 * @version RegistryContainer.java, v 0.1 2019年07月09日 14:28 luochang Exp
 */
public class RegistryContainer  {


    private  Map<String, KearpcReference> referenceSubscribeMap = new HashMap<>();


    public void publishAllConfig(KearpcProperties properties, Registry registry){

        if(!properties.isRegistry()){
            return;
        }

        registry.setAddress(properties.getRegistryAddress());
        registry.start();

        Set<Map.Entry<String, KearpcService>> entries = ServiceCache.getCache().get().entrySet();
        for (Map.Entry<String, KearpcService> entry : entries) {

            if(entry.getValue().isRegistry()){
                String address = properties.getServerAddress();
                Provider provider = new Provider(LaniakeaKit.ip(address),
                        LaniakeaKit.port(address),entry.getKey(),
                        properties.getProtocol());

                registry.register(provider);
            }
        }

        for (Map.Entry<String, KearpcReference> entry : referenceSubscribeMap.entrySet()) {

                Consumer consumer = new Consumer();
                consumer.setRemoteKey(entry.getValue().uniqueId());
                consumer.setNativeKey(entry.getKey());
                registry.subscribe(consumer);

        }

    }

    public void publishCheck() throws IllegalArgumentException {

        Map<String, KearpcReference> referenceMap = ReferenceCache.getCache().get();
        for (Map.Entry<String, KearpcReference> entry : referenceMap.entrySet()) {
            KearpcReference reference = entry.getValue();
            if(!reference.isSubscribe()){

                if(StringUtils.isEmpty(reference.address())) {
                    throw new IllegalArgumentException("Non-subscription requires a specified address");
                }

                CopyOnWriteArrayList channels;

                if (null == MassageClientExecutor.ME.getChannel().get(entry.getKey())) {
                    channels = new CopyOnWriteArrayList<>();
                }else{
                    channels = (CopyOnWriteArrayList) MassageClientExecutor.ME.getChannel().get(entry.getKey());
                }

                if(!AddressCache.getCache().contains(reference.address())){
                    String host = ip(reference.address());
                    int port = port(reference.address());

                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    ConsumerConfig clientInfo = new ConsumerConfig(
                            new InetSocketAddress(host, port),
                            KearpcSerializeProtocol.valueOf(reference.protocol()),
                            countDownLatch);
                    MassageClientExecutor.ME.setClientProperties(clientInfo).start();

                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                channels.add(AddressCache.getCache().get(reference.address()));
                MassageClientExecutor.ME.getChannel().put(entry.getKey(),channels);

            }else{
                referenceSubscribeMap.put(entry.getKey(),entry.getValue());
            }
        }
    }
}
