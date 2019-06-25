package com.laniakea.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;


import static com.laniakea.config.KearpcConstants.APPLICATION_PROPERTIES;
import static com.laniakea.config.KearpcConstants.defaulscan;

/**
 * @author luochang
 * @version ClientAnnotationImportBeanDefinitionRegistrar.java, v 0.1 2019年06月21日 18:32 luochang Exp
 */
@Conditional(ClientCondition.class)
public class ClientAnnotationImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(APPLICATION_PROPERTIES);
        } catch (IOException e) {
            properties = new Properties();
        }

        ReferenceCandidateComponentScanner scanner = new ReferenceCandidateComponentScanner(true,properties);

        AnnotationMetadata metadata = AnnotationRegisryMatcher
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

        scanner.scan(registry, packages.toArray(new String[packages.size()]) );
    }


}
