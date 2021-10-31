package com.zyj.test;

import com.spring.AnnotationConfigApplicationContext;
import com.zyj.ApplicationConfig;
import com.zyj.service.IUserService;

//UserService.class --> 无参构造方法(推断构造方法) --> 对象 --> 依赖注入(属性赋值) --> 初始化前(@PostConstruct) --> 初始化(InitializingBean) --> 初始化后(AOP) --> Bean
/**
 * @Author: yjzhong
 * @Date: 2021-10-24 15:18
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        IUserService userService = (IUserService) context.getBean("userService");
        userService.test();
    }
}
