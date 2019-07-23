package com.visy.spring.aop.my;

import java.lang.reflect.Method;

public class MyInvocationHandlerImpl implements MyInvocationHandler{
    private Object target;

    public MyInvocationHandlerImpl(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行前...");
        Object result = method.invoke(this.target, args);
        System.out.println("执行后");
        return result;
    }
}
