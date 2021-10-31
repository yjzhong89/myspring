package com.zyj.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

import java.lang.reflect.Proxy;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beanName.equals("detailService")) {
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                System.out.println("---切面---");
                return method.invoke(bean, args);
            });
            return proxyInstance;
        }
        return bean;
    }
}
