package com.zyj;

import com.spring.AnnotationConfigApplicationContext;

/**
 * @Author: yjzhong
 * @Date: 2021-10-24 15:18
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }
}
