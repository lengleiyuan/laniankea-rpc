package com.laniakea.spring;

import com.laniakea.annotation.KearpcService;
import com.laniakea.cache.MessageCache;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author wb-lgc489196
 * @version ServiceBeanPostProcess.java, v 0.1 2019年07月09日 16:12 wb-lgc489196 Exp
 */

public class ServiceBeanPostProcess implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        KearpcService kearpcService = beanClass.getAnnotation(KearpcService.class);

        if(null == kearpcService){
            return bean;
        }

        MessageCache.getCache().put(bean.getClass().getName(),bean);

        return bean;
    }
}
