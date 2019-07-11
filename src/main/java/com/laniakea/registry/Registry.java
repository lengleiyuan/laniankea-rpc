package com.laniakea.registry;

import com.laniakea.registry.zk.Consumer;
import com.laniakea.registry.zk.Provider;
import com.laniakea.registry.zk.ProviderGroup;

/**
 * @author luochang
 * @version Registry.java, v 0.1 2019年07月03日 16:49 luochang Exp
 */
public interface Registry {

     void start();

     void register(Provider provider);


     void register(ProviderGroup providerGroup);


     void unRegister(Provider provider);


     void unRegister(ProviderGroup providerGroup);


     ProviderGroup subscribe(Consumer consumer);


     void unSubscribe(Consumer consumer);


     void destroy();

     void setAddress(String address);



}
