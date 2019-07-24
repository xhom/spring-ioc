package com.visy.spring.aop.my;

import com.visy.spring.aop.usecase.Developer;
import com.visy.spring.aop.usecase.JavaDeveloper;
import com.visy.spring.aop.usecase.People;

public class Test {
    public static void main(String[] args){
        JavaDeveloper developer = new JavaDeveloper("旺旺");

        Object developerProxy = MyProxy.newProxyInstance(developer.getClass().getInterfaces(), new MyProxyHandler(developer));
        System.out.println("==================================");

        People proxy1 = (People)developerProxy;
        proxy1.eat();
        System.out.println(proxy1.getHeight("asas",101));
        Developer proxy2 = (Developer)developerProxy;
        proxy2.coding();
        proxy2.debugging();
    }
}
