package com.visy.spring.service.impl;

import com.visy.spring.annotation.Component;
import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

import javax.annotation.Resource;

@Component("userService")
public class UserServiceImpl implements UserService {
    private UserDAO userDao1;
    @Resource
    private UserDAO userDao2;
    @Resource(name="userDAO3")
    private UserDAO userDao3;
    @Resource
    private UserDAO userDao4;

    public UserDAO getUserDao1() {
        return userDao1;
    }

    public void setUserDao1(UserDAO userDao1) {
        this.userDao1 = userDao1;
    }

    public UserDAO getUserDao2() {
        return userDao2;
    }

    public void setUserDao2(UserDAO userDao2) {
        this.userDao2 = userDao2;
    }


    public UserDAO getUserDao3() {
        return userDao3;
    }

    public void setUserDao3(UserDAO userDao3) {
        this.userDao3 = userDao3;
    }

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
