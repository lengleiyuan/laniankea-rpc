package com.laniakea.spring;

import com.laniakea.annotation.KearpcClient;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wb-lgc489196
 * @version ClientCondition.java, v 0.1 2019年05月31日 14:47 wb-lgc489196 Exp
 */
public class ClientCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        AnnotationMetadata annotationMetadata = AnnotationRegisryMatcher
                .registryfindAnnotationMetadata(context.getRegistry(),KearpcClient.class.getName());

        return null != annotationMetadata ? AnnotationRegisryMatcher.regisryMatchesAnnotation(annotationMetadata) : false;
    }

}
