package com.laniakea.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * @author luochang
 * @version KearpcEvent.java, v 0.1 2019年07月09日 15:22 luochang Exp
 */
public class KearpcBootEvent extends ApplicationContextEvent {
    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for (must not be {@code null})
     */
    public KearpcBootEvent(ApplicationContext source) {
        super(source);
    }
}
