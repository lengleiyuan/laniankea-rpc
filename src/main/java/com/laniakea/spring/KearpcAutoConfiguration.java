package com.laniakea.spring;

import com.laniakea.config.KearpcProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * @author luochang
 * @version KearpcAutoConfiguration.java, v 0.1 2019年05月29日 15:16 luochang Exp
 */
@Configuration
@Import(ClientAnnotationImportBeanDefinitionRegistrar.class)
@EnableConfigurationProperties(value = {KearpcProperties.class})
public class KearpcAutoConfiguration {



    @Bean
    @Conditional(ServerCondition.class)
    public RegistryInitialize rpcRegistryInitialize(KearpcProperties kearpcProperties){
        RegistryInitialize rpcRegistryInitialize = new RegistryInitialize();
        rpcRegistryInitialize.setKearpcProperties(kearpcProperties);
        return rpcRegistryInitialize;
    }


    @Bean
    public BeanPostProcessor kearpcServiceAnnotationPostProcessor(){
        return new ServiceAnnotationPostProcessor();
    }



}
