package com.king.bishe.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Bean工具类
 * springboot获取Spring容器中的bean
 * 在非spring管理的类中获取spring注册的bean
 */
@Slf4j
@Lazy(false)
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    public SpringBeanUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.info("初已始化ApplicationContext。。。");
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        if(applicationContext==null){
            System.out.println("applicationContext is null !!!!!!!!!!!!!!!!");
        }
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
