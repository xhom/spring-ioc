package com.visy.spring.test;


import com.visy.spring.core.BeanFactory;
import com.visy.spring.dao.UserDAO;
import com.visy.spring.model.User;
import com.visy.spring.service.impl.UserServiceImpl;

/**
 * 通过bean工厂代替new创建对象
 */
public class AddUserTest2 {
    public static void main(String[] args) {
        //通过Bean工厂获取对象
        UserServiceImpl userService = (UserServiceImpl) BeanFactory.getBean("userService");
        UserDAO userDao1 = (UserDAO) BeanFactory.getBean("userDAO1");

        userService.setUserDao1(userDao1);//设置数据持久化对象

        //创建要保存的用户
        User user = new User(2,"李四","002");

        userService.add1(user);//添加用户
    }
}
