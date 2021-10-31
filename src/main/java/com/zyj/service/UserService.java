package com.zyj.service;

import com.spring.annotation.Component;

@Component
public class UserService implements IUserService {
    @Override
    public void test() {
        System.out.println("---test---");
    }
}
