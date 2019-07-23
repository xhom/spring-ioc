package com.visy.spring.aop;

public class JavaDeveloper implements Developer,People{
    private String name;

    public JavaDeveloper(String name) {
        this.name = name;
    }

    public void coding() {
        System.out.println(this.name+"正在用Java写代码");
    }

    public void debugging() {
        System.out.println(this.name+"正在修改Java的BUG");
    }

    public void eat() {
        System.out.println(this.name+"正在吃饭...");
    }
}
