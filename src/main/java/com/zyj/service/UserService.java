package com.zyj.service;

import com.spring.InitializingBean;
import com.spring.annotation.Component;

@Component
public class UserService implements IUserService, InitializingBean {
    @Override
    public void test() {
        System.out.println("---test---");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("---init---");
    }
}
