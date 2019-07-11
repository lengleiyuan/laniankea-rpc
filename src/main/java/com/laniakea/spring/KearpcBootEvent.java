package com.laniakea.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * @author wb-lgc489196
 * @version KearpcEvent.java, v 0.1 2019年07月09日 15:22 wb-lgc489196 Exp
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
