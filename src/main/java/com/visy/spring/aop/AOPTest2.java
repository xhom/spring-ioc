package com.visy.spring.aop;

public class AOPTest2 {
    public static void main(String[] args) {
        JavaDeveloper wang = new JavaDeveloper("汪汪");
        wang.coding();

        System.out.println("==============================");

        Developer wangProxy2 = ProxyHandler.newInstance(wang).proxy(Developer.class);
        wangProxy2.coding();
        wangProxy2.debugging();

        System.out.println("==============================");

        People wangProxy = ProxyHandler.newInstance(wang).proxy(People.class);
        wangProxy.eat();

        System.out.println("==============================");

        System.out.println(wang.hashCode());
        System.out.println(wangProxy.hashCode());
        System.out.println(wangProxy2.hashCode());

        System.out.println("==============================");

        System.out.println(wang == wangProxy);
        System.out.println(wang == wangProxy2);
        System.out.println(wangProxy == wangProxy2);
    }
}
