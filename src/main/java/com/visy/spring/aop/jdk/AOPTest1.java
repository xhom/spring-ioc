package com.visy.spring.aop.jdk;

import com.visy.spring.aop.usecase.Developer;
import com.visy.spring.aop.usecase.JavaDeveloper;
import com.visy.spring.aop.usecase.People;

import java.lang.reflect.Proxy;

public class AOPTest1 {
    public static void main(String[] args) {
        JavaDeveloper wang = new JavaDeveloper("汪汪");

        Class<?> clazz = wang.getClass();
        ClassLoader loader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();

        Object wangProxy = Proxy.newProxyInstance(loader,interfaces,(proxy, method, args1) -> {
            if("coding".equals(method.getName())){
                System.out.println("汪汪正在做祷告,祈祷风调码顺...");
                Object result = method.invoke(wang, args1);
                System.out.println("汪汪代码写完了，好厉害！！！");
                return result;
            }
            if("debugging".equals(method.getName())){
                System.out.println("汪汪没有写BUG.不用改了");
                return null;
            }
            if("eat".equals(method.getName())){
                System.out.println("汪汪饿了，汪汪不想写代码了");
                return method.invoke(wang, args1);
            }

            return method.invoke(args1);
        });

        ((Developer)wangProxy).coding();
        ((Developer)wangProxy).debugging();
        ((People)wangProxy).eat();
    }
}
