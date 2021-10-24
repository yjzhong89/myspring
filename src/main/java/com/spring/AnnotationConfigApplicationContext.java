package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.spring.Constants.FILE_SEPERATOR;

/**
 * @Author: yjzhong
 * @Date: 2021-10-24 15:16
 */
public class AnnotationConfigApplicationContext {
    private Class config;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(16);
    private Map<String, Object> singletonObjects = new HashMap<>(16);
    private List<BeanPostProcessor> beanPostProcessors = new LinkedList<>();


    public AnnotationConfigApplicationContext(Class config) {
        this.config = config;

        // 在指定目录中进行class的扫描
        scan(config);
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            if (instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // 扩展点：bean的前置处理器
            for (BeanPostProcessor beanPostProcessor: beanPostProcessors) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // 扩展点：bean的后置处理器
            for (BeanPostProcessor beanPostProcessor: beanPostProcessors) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new RuntimeException("No available bean found!");
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope())) {
            // 单例bean
            Object singletonObject = singletonObjects.get(beanName);
            if (singletonObject == null) {
                singletonObject = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonObject);
            }
            return singletonObject;
        } else {
            // 原型bean
            Object prototype = createBean(beanName, beanDefinition);
            return prototype;
        }
    }

    /**
     * 1. 根据配置类获取扫描的根路径
     * 2. 解析该路径下的所有类，并找到所有符合条件的类
     *
     * @param config
     */
    private void scan(Class config) {
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String rootPath = componentScan.value();
            rootPath = rootPath.replace(".", "/");

            ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
            URL resourceUrl = classLoader.getResource(rootPath);
            File directory = new File(resourceUrl.getFile());

            if (directory.isDirectory()) {
                for (File classFile : directory.listFiles()) {
                    String absolutePath = classFile.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("classes") + 8, absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace(FILE_SEPERATOR, ".");

                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);

                        // 先判断是否添加了@Component注解
                        if (clazz.isAnnotationPresent(Component.class)) {

                            // 判断是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                beanPostProcessors.add(beanPostProcessor);
                            }

                            Component componentAnnotation = clazz.getAnnotation(Component.class);

                            // 设置beanName
                            String beanName = componentAnnotation.value();
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);

                            // 判断是否有@Scope注解
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String scopeValue = scopeAnnotation.value();
                                beanDefinition.setScope(scopeValue);
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                            // 将解析出来的bean加入beanDefinitionMap进行缓存
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
