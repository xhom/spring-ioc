package com.visy.spring.aop.my;

public class UserServiceImpl implements UserService{
    @Override
    public void execute() {
        System.out.println("正在执行...");
    }
}
