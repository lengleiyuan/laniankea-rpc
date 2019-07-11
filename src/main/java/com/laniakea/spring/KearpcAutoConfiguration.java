package com.laniakea.spring;

import com.laniakea.config.KearpcProperties;
import com.laniakea.registry.Registry;
import com.laniakea.registry.zk.ZookeeperRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author luochang
 * @version KearpcAutoConfiguration.java, v 0.1 2019年05月29日 15:16 luochang Exp
 */
@Configuration
@Import(ClientAnnotationImportBeanDefinitionRegistrar.class)
@EnableConfigurationProperties(value = {KearpcProperties.class})
@Conditional(KearpcCondition.class)
public class KearpcAutoConfiguration {


    @Bean
    public Registry registry(){
        return new ZookeeperRegistry();
    }

    @Bean
    public RegistryContainer registryContainer(){
        return new RegistryContainer();
    }

    @Bean
    public BrokerContainer brokerContainer(){
        return new BrokerContainer();
    }

    @Bean
    public ApplicationContextRefreshedListener applicationContextRefreshedListener(){
        return new ApplicationContextRefreshedListener();
    }

    @Bean
    public KearpcStartListener kearpcStartListener(KearpcProperties properties,
                                                   Registry registry,
                                                   RegistryContainer container,
                                                   BrokerContainer brokerContainer){
        return new KearpcStartListener(properties,registry,container,brokerContainer);
    }


    @Bean
    public ServiceBeanPostProcess serviceBeanPostProcess(){
        return new ServiceBeanPostProcess();
    }

    @Bean
    public ApplicationContextClosedListener applicationContextClosedListener(BrokerContainer brokerContainer, Registry registry){
        return new ApplicationContextClosedListener(brokerContainer,registry);
    }






}
