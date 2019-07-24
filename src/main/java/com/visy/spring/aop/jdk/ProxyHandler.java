package com.visy.spring.aop.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {
    private Object target;

    private ProxyHandler(Object target) {
        this.target = target;
    }

    public static ProxyHandler newInstance(Object target){
        return new ProxyHandler(target);
    }


    public <T> T proxy(Class<T> t){
        if(this.target == null){
            return null;
        }
        Class<?> clazz = this.target.getClass();
        ClassLoader loader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(loader,interfaces,this);
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

        return method.invoke(this.target,args);
    }
}
