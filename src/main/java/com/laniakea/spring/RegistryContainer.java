package com.laniakea.spring;

import com.laniakea.annotation.KearpcReference;
import com.laniakea.annotation.KearpcService;
import com.laniakea.cache.ReferenceCache;
import com.laniakea.cache.ServiceCache;
import com.laniakea.config.KearpcProperties;
import com.laniakea.executor.MassageClientExecutor;
import com.laniakea.kit.LaniakeaKit;
import com.laniakea.registry.Registry;
import com.laniakea.registry.zk.Consumer;
import com.laniakea.registry.zk.Provider;
import com.laniakea.serialize.KearpcSerializeProtocol;
import io.netty.channel.Channel;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.laniakea.kit.LaniakeaKit.ip;
import static com.laniakea.kit.LaniakeaKit.port;

/**
 * @author wb-lgc489196
 * @version RegistryContainer.java, v 0.1 2019年07月09日 14:28 wb-lgc489196 Exp
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
                consumer.setUniqueId(entry.getValue().uniqueId());
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

                Map<String, Channel> channel = MassageClientExecutor.ME.getChannel();

                if(channel.containsKey(reference.address())){
                    return;
                }

                String host = ip(reference.address());
                int port = port(reference.address());

                InetSocketAddress socketAddress = new InetSocketAddress(host,port);
                MassageClientExecutor.ME.setClientProperties(socketAddress,
                        KearpcSerializeProtocol.valueOf(reference.protocol())).start();

            }else{
                referenceSubscribeMap.put(entry.getKey(),entry.getValue());
            }
        }
    }
}
