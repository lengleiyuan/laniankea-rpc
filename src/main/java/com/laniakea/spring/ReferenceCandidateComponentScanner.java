package com.laniakea.spring;

import com.laniakea.config.ClientProxy;
import com.laniakea.annotation.KearpcReference;
import com.laniakea.core.SemaphoreCache;
import com.laniakea.exection.KearpcException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Iterator;
import java.util.Set;

import static com.laniakea.config.KearpcConstants.*;

/**
 * @author luochang
 * @version ReferenceCandidateComponentScanner.java, v 0.1 2019年06月20日 14:53 luochang Exp
 */
public class ReferenceCandidateComponentScanner  extends ClassPathScanningCandidateComponentProvider {


    public ReferenceCandidateComponentScanner(boolean useDefaultFilters) {
        super(useDefaultFilters);
    }
    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(KearpcReference.class));
    }

    public void scan(BeanDefinitionRegistry registry, String... basePackages) {

        for (String basePackage : basePackages) {

            Set<BeanDefinition> beanDefinitions = findCandidateComponents(basePackage);
            for (Iterator<BeanDefinition> iterator = beanDefinitions.iterator(); iterator.hasNext(); ) {

                BeanDefinition definition = iterator.next();

                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
                AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotatedBeanDefinition.getMetadata()
                        .getAnnotationAttributes(KearpcReference.class.getName(), true));

                String protocol = attributes.getString(PROTOCOL);
                String interfaceName = attributes.getString(INTERFACENAME);
                String address = attributes.getString(ADDRESS);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(definition.getBeanClassName());
                GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();

                ClientProxy clientProxy = new ClientProxy(interfaceName, definition.getBeanClassName(),
                       ip(address),Integer.valueOf(port(address)),protocol);
                SemaphoreCache.getCache().init(definition.getBeanClassName());
                genericBeanDefinition.getPropertyValues().addPropertyValue(PROXYPROPERTIES,clientProxy);
                genericBeanDefinition.setBeanClass(ReferenceInitializeFactory.class);
                genericBeanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_TYPE);
                registry.registerBeanDefinition(simpleClassName(definition.getBeanClassName()), genericBeanDefinition);

            }
        }
    }
    


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isAnnotated(KearpcReference.class.getName()) && metadata.isInterface();
    }

}
