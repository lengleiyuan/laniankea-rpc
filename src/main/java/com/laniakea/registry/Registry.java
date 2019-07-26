package com.laniakea.registry;

import com.laniakea.core.Switch;

/**
 * @author luochang
 * @version Registry.java, v 0.1 2019年07月03日 16:49 luochang Exp
 */
public abstract class Registry implements Switch {

     protected String address;


     protected void setAddress(String address) {
          this.address = address;
     }

     protected abstract void register(Provider provider);


     protected abstract void register(ProviderGroup providerGroup);


     protected abstract void unRegister(Provider provider);


     protected abstract void unRegister(ProviderGroup providerGroup);


     protected abstract ProviderGroup subscribe(Consumer consumer);


     protected abstract void unSubscribe(Consumer consumer);





}
