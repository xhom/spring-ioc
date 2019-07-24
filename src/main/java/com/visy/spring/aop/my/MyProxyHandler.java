package com.visy.spring.aop.my;

import java.lang.reflect.Method;

public class MyProxyHandler implements MyInvocationHandler{
    private Object target;

    public MyProxyHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("coding".equals(method.getName())){
            System.out.println("汪汪正在祈祷风调码顺...");
            Object result = method.invoke(this.target, args);
            System.out.println("汪汪代码写完了，好厉害！！！");
            return result;
        }
        if("debugging".equals(method.getName())){
            System.out.println("汪汪没有写BUG.不用改了^_^");
            return null;
        }
        if("eat".equals(method.getName())){
            System.out.println("汪汪饿了，汪汪不想写代码了T_T");
            return method.invoke(this.target, args);
        }
        return method.invoke(this.target, args);
    }

    private void before(){
        System.out.println("执行前...");
    }

    private void after(){
        System.out.println("执行后");
    }
}
