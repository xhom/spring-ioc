package com.visy.spring.dao.impl;

import com.visy.spring.annotation.Component;
import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;

@Component("userDao4")
public class UserDAOImpl4 implements UserDAO {
    public void save(User user) {
        System.out.println("user saved[4]: "+ user.toString());
    }
}
