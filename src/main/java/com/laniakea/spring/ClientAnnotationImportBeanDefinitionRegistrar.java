package com.laniakea.spring;

import com.laniakea.annotation.KearpcReference;
import com.laniakea.annotation.KearpcService;
import com.laniakea.cache.ReferenceCache;
import com.laniakea.cache.ServiceCache;
import com.laniakea.kit.AnnotationMatcher;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.laniakea.config.KearpcConstants.PROXYPROPERTIES;
import static com.laniakea.kit.LaniakeaKit.defaulscan;
import static com.laniakea.kit.LaniakeaKit.simpleClassName;

/**
 * @author luochang
 * @version ClientAnnotationImportBeanDefinitionRegistrar.java, v 0.1 2019年06月21日 18:32 luochang Exp
 */
public class ClientAnnotationImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry){


        AnnotationMetadata metadata = AnnotationMatcher
                .registryfindAnnotationMetadata(registry, ComponentScan.class.getName());
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(ComponentScan.class.getName(), true));

        Set<String> packages = new LinkedHashSet<>();

        if(null != attributes){
            Collections.addAll(packages, attributes.getStringArray("value"));
            Collections.addAll(packages, attributes.getStringArray("basePackages"));
            Collections.addAll(packages, attributes.getStringArray("basePackageClasses"));
        }

        if(0 == packages.size()){
            packages.add(defaulscan(metadata.getClassName()));
        }

        resolveReference(packages,registry);
        resolveService(packages);

    }



    private  Class<?> resolveBeanClassType(BeanDefinition beanDefinition) {
        Class<?> clazz = null;

        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) beanDefinition)
                    .getMetadata();
            try {
                String className = annotationMetadata.getClassName();
                clazz = StringUtils.isEmpty(className) ? null : ClassUtils.forName(className, null);
            } catch (Throwable throwable) {
                // ignore
            }
        }

        if (clazz == null) {
            try {
                clazz = ((AbstractBeanDefinition) beanDefinition).getBeanClass();
            } catch (IllegalStateException ex) {
                try {
                    String className = beanDefinition.getBeanClassName();
                    clazz = StringUtils.isEmpty(className) ? null : ClassUtils.forName(className,
                            null);
                } catch (Throwable throwable) {
                    // ignore
                }
            }
        }

        if (ClassUtils.isCglibProxyClass(clazz)) {
            return clazz.getSuperclass();
        } else {
            return clazz;
        }
    }

    private void resolveReference(Set<String> packages,BeanDefinitionRegistry registry){

        ReferenceCandidateComponentScanner referenceScanner =
                new ReferenceCandidateComponentScanner(KearpcReference.class);

        Set<BeanDefinition> referenceSet = referenceScanner.scan(packages.toArray(new String[packages.size()]));

        for (Iterator<BeanDefinition> iterator = referenceSet.iterator(); iterator.hasNext(); ) {

            BeanDefinition definition = iterator.next();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(definition.getBeanClassName());
            GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            genericBeanDefinition.getPropertyValues().addPropertyValue(PROXYPROPERTIES,definition.getBeanClassName());
            genericBeanDefinition.setBeanClass(ReferenceFactory.class);
            genericBeanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(simpleClassName(definition.getBeanClassName()), genericBeanDefinition);


            Class<?> aClass = resolveBeanClassType(definition);
            KearpcReference reference = aClass.getAnnotation(KearpcReference.class);
            Map<String, KearpcReference> referenceMap = ReferenceCache.getCache().get();
            referenceMap.putIfAbsent(aClass.getName(),reference);
        }
    }

    private void resolveService(Set<String> packages){

        ReferenceCandidateComponentScanner serviceScanner =
                new ReferenceCandidateComponentScanner( KearpcService.class);

        Set<BeanDefinition> serviceSet = serviceScanner.scan(packages.toArray(new String[packages.size()]));
        for (Iterator<BeanDefinition> iterator = serviceSet.iterator(); iterator.hasNext(); ) {
            BeanDefinition definition = iterator.next();
            Class<?> aClass = resolveBeanClassType(definition);
            KearpcService service = aClass.getAnnotation(KearpcService.class);
            Map<String, KearpcService> serviceMap = ServiceCache.getCache().get();
            serviceMap.putIfAbsent(aClass.getName(), service);
        }
    }
}
