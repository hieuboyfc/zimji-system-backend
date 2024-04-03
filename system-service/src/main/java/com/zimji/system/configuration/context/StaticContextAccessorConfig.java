package com.zimji.system.configuration.context;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Configuration
public class StaticContextAccessorConfig {

    @Bean
    public StaticContextAccessor staticContextAccessor() {
        return new StaticContextAccessor();
    }

    @Component
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StaticContextAccessor implements ApplicationContextAware, BeanFactoryPostProcessor {

        static ApplicationContext applicationContext;
        static ConfigurableListableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            StaticContextAccessor.applicationContext = applicationContext;
        }

        @Override
        public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
            StaticContextAccessor.beanFactory = beanFactory;
        }

        public static <T> T getBean(Class<T> clazz) {
            T bean = applicationContext.getBean(clazz);
            if (ObjectUtils.isEmpty(bean)) {
                return createProxy(clazz);
            }
            return bean;
        }

        public static <T> T getBean(String beanName, Class<T> clazz) {
            T bean = applicationContext.getBean(beanName, clazz);
            if (ObjectUtils.isEmpty(bean)) {
                return createProxy(clazz);
            }
            return bean;
        }

        public static <T> T createProxy(Class<T> targetType) {
            InvocationHandler handler = (proxy, method, args) -> method.invoke(null, args);
            return targetType.cast(
                    Proxy.newProxyInstance(
                            targetType.getClassLoader(),
                            new Class[]{targetType},
                            handler
                    )
            );
        }

    }

}