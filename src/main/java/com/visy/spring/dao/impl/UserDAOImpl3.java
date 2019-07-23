package com.visy.spring.dao.impl;

import com.visy.spring.annotation.Component;
import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;

@Component
public class UserDAOImpl3 implements UserDAO {
    public void save(User user) {
        System.out.println("user saved[3]: "+ user.toString());
    }
}
