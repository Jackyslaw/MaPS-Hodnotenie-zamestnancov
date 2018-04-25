package com.hackag.fibimeter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Allows Spring Beans to be located from static context.
 * <p>
 * Created by Steve on 21.10.2017.
 */
@Component
public class StaticContextAccessor {

    private static StaticContextAccessor instance;

    private ApplicationContext applicationContext;

    @Autowired
    public StaticContextAccessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }
}
