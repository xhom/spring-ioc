package com.visy.spring.test;

import com.visy.spring.core.BeanFactory;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

/**
 *  通过annotation配置实现自动注入属性值-set（1）
 */
public class AddUserTest4 {
    public static void main(String[] args) {

        //通过Bean工厂获取对象
        UserService userService = (UserService)BeanFactory.getBean2("userService");

        //创建要保存的用户
        User user = new User(4,"陈六","004");

        userService.add3(user);//添加用户
    }
}
