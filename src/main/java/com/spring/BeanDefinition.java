package com.spring;

/**
 * @Author: yjzhong
 * @Date: 2021-10-24 14:50
 */
public class BeanDefinition {
    private Class type;
    private String scope;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
