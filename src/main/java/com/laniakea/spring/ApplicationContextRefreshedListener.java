package com.laniakea.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author wb-lgc489196
 * @version ApplicationContextRefreshedListener.java, v 0.1 2019年07月09日 15:20 wb-lgc489196 Exp
 */
public class ApplicationContextRefreshedListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {

            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            applicationContext.publishEvent(new KearpcBootEvent(applicationContext));

        }
    }
}
