package com.laniakea.spring;

import com.laniakea.annotation.KearpcService;
import com.laniakea.core.MessageCache;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author luochang
 * @version ServiceAnnotationPostProcessor.java, v 0.1 2019年05月30日 10:59 luochang Exp
 */
public class ServiceAnnotationPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        KearpcService kearpcService = beanClass.getAnnotation(KearpcService.class);

        if(null == kearpcService){
            return bean;
        }

        MessageCache.getCache().putHandler(bean.getClass().getName(),bean);

        return bean;
    }
}
