package com.visy.spring.dao.impl;

import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;

public class UserDAOImpl2 implements UserDAO {
    public void save(User user) {
        System.out.println("user saved[2]: "+ user.toString());
    }
}
