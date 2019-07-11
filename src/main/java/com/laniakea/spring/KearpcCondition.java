package com.laniakea.spring;

import com.laniakea.annotation.EnableKearpc;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author luochang
 * @version ServerCondition.java, v 0.1 2019年05月31日 14:47 luochang Exp
 */
public class KearpcCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        AnnotationMetadata annotationMetadata = AnnotationRegisryMatcher
                .registryfindAnnotationMetadata(context.getRegistry(), EnableKearpc.class.getName());

        return null != annotationMetadata ? AnnotationRegisryMatcher.regisryMatchesAnnotation(annotationMetadata) : false;

    }

}
