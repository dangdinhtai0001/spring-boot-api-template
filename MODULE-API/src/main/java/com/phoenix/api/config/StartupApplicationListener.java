package com.phoenix.api.config;

import lombok.extern.log4j.Log4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String name = contextRefreshedEvent.getApplicationContext().getId();
        int beanCount = contextRefreshedEvent.getApplicationContext().getBeanDefinitionCount();
        log.info(String.format("STARTED APPLICATION: %s with %d beans", name, beanCount));
    }
}
