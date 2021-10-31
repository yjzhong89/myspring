package com.zyj.test;

import com.spring.AnnotationConfigApplicationContext;
import com.zyj.ApplicationConfig;
import com.zyj.service.IDetailService;

public class AopTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        IDetailService detailService = (IDetailService) context.getBean("detailService");
        detailService.detail();
    }
}
