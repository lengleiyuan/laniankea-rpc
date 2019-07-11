package com.laniakea.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author luochang
 * @version ReferenceCandidateComponentScanner.java, v 0.1 2019年06月20日 14:53 luochang Exp
 */
public class ReferenceCandidateComponentScanner  extends ClassPathScanningCandidateComponentProvider {

    private Class<? extends Annotation> annotationType;

    public ReferenceCandidateComponentScanner(Class<? extends Annotation> annotationType) {
        super(false);
        super.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        this.annotationType = annotationType;
    }



    public  Set<BeanDefinition> scan(String... basePackages) {
        Set<BeanDefinition> definitions = new HashSet<>();
        Stream.of(basePackages).forEach(packages -> definitions.addAll(findCandidateComponents(packages)));
        return definitions;
    }
    

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isAnnotated(annotationType.getName());
    }




}
