package com.laniakea.registry.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.laniakea.exection.KearpcException;
import com.laniakea.registry.Consumer;
import com.laniakea.registry.Provider;
import com.laniakea.registry.ProviderGroup;
import com.laniakea.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.laniakea.config.KearpcConstants.SERIALIZE;

/**
 * @author wb-lgc489196
 * @version nacosRegistry.java, v 0.1 2019年07月15日 14:50 wb-lgc489196 Exp
 */
public class NacosRegistry extends Registry {

    private final static Logger LOGGER = LoggerFactory.getLogger(NacosRegistry.class);

    private NamingService namingService;

    private static final String DEFAULT_NAMESPACE = "laniakea-rpc";

    private Map<Provider, Instance> providerInstance = new ConcurrentHashMap<>();

    private Map<Consumer, EventListener> consumerListener = new ConcurrentHashMap<>();

    private Properties nacosConfig = new Properties();

    @Override
    public void start() {

        if(null != namingService){
            return;
        }

        nacosConfig.put(PropertyKeyConst.SERVER_ADDR, address);
        nacosConfig.put(PropertyKeyConst.NAMESPACE, DEFAULT_NAMESPACE);
        try {
            namingService = NamingFactory.createNamingService(nacosConfig);
        } catch (NacosException e) {
            throw new KearpcException("Init nacos naming service error, address: " + address);
        }
    }

    @Override
    public void register(Provider provider) {
        try {
            Instance instance;
            Instance optionalInstance = providerInstance.get(provider);
            if(null != optionalInstance){
                instance = optionalInstance;
            }else{
                instance = new Instance();
                instance.setInstanceId(provider.getUniqueId());
                instance.setIp(provider.getHost());
                instance.setPort(provider.getPort());
                instance.setMetadata(new HashMap() {{put(SERIALIZE, provider.getProtocol().name());}});
                providerInstance.putIfAbsent(provider,instance);
            }
            try {
                namingService.registerInstance(provider.getUniqueId(), instance);
            } catch (NacosException e) {
                LOGGER.warn(e.getErrMsg().concat("Failed to register provider to nacosRegistry"),e);
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to register provider to nacosRegistry",e);
        }
    }

    @Override
    public void register(ProviderGroup providerGroup) {
        providerGroup.getProviders().forEach(provider -> register(provider));
    }

    @Override
    public void unRegister(Provider provider) {
        String uniqueId = provider.getUniqueId();
        Instance instance = providerInstance.remove(provider);
        try {
            namingService.deregisterInstance(uniqueId, instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            throw new KearpcException("Failed to unregister provider to nacos registry! service: " + uniqueId,e);
        }
    }

    @Override
    public void unRegister(ProviderGroup providerGroup) {
        providerGroup.getProviders().forEach(provider -> unRegister(provider));
    }

    @Override
    public ProviderGroup subscribe(Consumer consumer) {
        return null;
    }

    @Override
    public void unSubscribe(Consumer consumer) {

    }

    @Override
    public void destroy() {

    }

}
