package com.visy.spring.aop.my;

import java.lang.reflect.Method;

public class MyProxyHandler implements MyInvocationHandler{
    private Object target;

    public MyProxyHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void before(){
        System.out.println("before...");
    }

    private void after(){
        System.out.println("after...");
    }
}
