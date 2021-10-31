package com.zyj.service;

import com.spring.InitializingBean;
import com.spring.annotation.Component;

@Component
public class DetailService implements IDetailService, InitializingBean {
    @Override
    public void afterPropertiesSet() {
        System.out.println("---init---");
    }

    @Override
    public void detail() {
        System.out.println("---detail---");
    }
}
