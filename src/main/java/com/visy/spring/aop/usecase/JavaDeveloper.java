package com.visy.spring.aop.usecase;

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

    @Override
    public int getHeight(String ss,int s) {
        System.out.println("身高。。。"+ss);
        return s;
    }

    @Override
    public Double weight() {
        System.out.println("体重。。。");
        return 12.33;
    }

    @Override
    public String getName() {
        System.out.println("获取名字。。。");
        return this.name;
    }
}
