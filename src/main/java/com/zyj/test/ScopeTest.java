package com.zyj.test;

import com.spring.AnnotationConfigApplicationContext;
import com.zyj.ApplicationConfig;
import com.zyj.service.OrderService;

public class ScopeTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        OrderService orderService1 = (OrderService) context.getBean("orderService");
        OrderService orderService2 = (OrderService) context.getBean("orderService");
        System.out.println(orderService1);
        System.out.println(orderService2);
    }
}
