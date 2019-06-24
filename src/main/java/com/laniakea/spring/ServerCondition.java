package com.laniakea.spring;

import com.laniakea.annotation.KearpcServer;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wb-lgc489196
 * @version ServerCondition.java, v 0.1 2019年05月31日 14:47 wb-lgc489196 Exp
 */
public class ServerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        AnnotationMetadata annotationMetadata = AnnotationRegisryMatcher
                .registryfindAnnotationMetadata(context.getRegistry(),KearpcServer.class.getName());

        return null != annotationMetadata ? AnnotationRegisryMatcher.regisryMatchesAnnotation(annotationMetadata) : false;

    }

}
