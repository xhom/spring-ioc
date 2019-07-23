package com.visy.spring.aop.my;

public class Test {
    public static void main(String[] args) throws Throwable{
        UserServiceImpl userService = new UserServiceImpl();
        MyInvocationHandlerImpl handler = new MyInvocationHandlerImpl(userService);

        UserService userServiceProxy = (UserService) MyProxy.newProxyInstance(Test.class.getClassLoader(),UserService.class,handler);

        userServiceProxy.execute();
    }
}
