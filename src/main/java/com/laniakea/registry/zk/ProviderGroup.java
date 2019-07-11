package com.laniakea.registry.zk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luochang
 * @version ProvierGroup.java, v 0.1 2019年07月04日 15:23 luochang Exp
 */
public class ProviderGroup {

    private List<Provider> providers = new ArrayList<>();

    public ProviderGroup add(Provider provider){
        providers.add(provider);
        return this;
    }

    public ProviderGroup setProviders(List<Provider> providers){
        this.providers = providers;
        return this;
    }

    public List<Provider> getProviders() {
        return providers;
    }
}
