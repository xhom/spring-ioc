package com.visy.spring.test;

import com.visy.spring.model.User;
import com.visy.spring.dao.impl.UserDAOImpl1;
import com.visy.spring.service.impl.UserServiceImpl;

/**
 * 不使用spring的原始添加用户写法
 */
public class AddUserTest1 {
    public static void main(String[] args) {
        //手动创建对象
        UserServiceImpl userService = new UserServiceImpl();
        UserDAOImpl1 userDao1 = new UserDAOImpl1();

        userService.setUserDao1(userDao1);//设置数据持久化对象

        //创建要保存的用户
        User user = new User(1,"张三","001");

        userService.add1(user);//调用添加方法
    }
}
