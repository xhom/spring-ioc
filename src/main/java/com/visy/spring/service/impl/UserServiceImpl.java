package com.visy.spring.service.impl;

import com.visy.spring.annotation.Component;
import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

import javax.annotation.Resource;

@Component("userService")
public class UserServiceImpl implements UserService {
    //手动设置
    private UserDAO userDao1;
    //使用xml setter注入
    private UserDAO userDao2;
    //使用annotation setter注入
    @Resource(name="userDAOImpl3")
    private UserDAO userDao3;
    //使用annotation 属性注入
    @Resource
    private UserDAO userDao4;

    // 手动设置
    public void setUserDao1(UserDAO userDao1) {
        this.userDao1 = userDao1;
    }
    // 供注入使用的setter
    public void setUserDao2(UserDAO userDao2) {
        this.userDao2 = userDao2;
    }
    public void setUserDao3(UserDAO userDao3) {
        this.userDao3 = userDao3;
    }

    //通过不同的dao添加用户
    public void add1(User user) {
        userDao1.save(user);
    }
    public void add2(User user) {
        userDao2.save(user);
    }
    public void add3(User user) {
        userDao3.save(user);
    }
    public void add4(User user) {
        userDao4.save(user);
    }
}
