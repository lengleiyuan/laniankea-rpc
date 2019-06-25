package com.laniakea.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author luochang
 * @version AnnotationRegisryMatcher.java, v 0.1 2019年05月28日 18:47 luochang Exp
 */
public final class AnnotationRegisryMatcher {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public static AnnotationMetadata registryfindAnnotationMetadata(BeanDefinitionRegistry registry,String annotationName) {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            String className = beanDef.getBeanClassName();
            if (className == null || beanDef.getFactoryMethodName() != null) {
                continue;
            }
            AnnotationMetadata metadata = beanDefinitionfindmetadata(beanDef, annotationName);
            if(null != metadata){
                return metadata;
            }
        }
        return null;
    }


    public static AnnotationMetadata beanDefinitionfindmetadata(BeanDefinition definition,String annotationName){
        if (definition instanceof AnnotatedBeanDefinition &&
                definition.getBeanClassName().equals(((AnnotatedBeanDefinition) definition).getMetadata().getClassName())){
            AnnotationMetadata metadata = ((AnnotatedBeanDefinition) definition).getMetadata();
            if(metadata.isAnnotated(annotationName)){
                return metadata;
            }
        }
        return null;
    }

    public static boolean regisryMatchesAnnotation(AnnotationMetadata metadata) {
        return metadata.isAnnotated(SpringBootApplication.class.getName());
    }

}
