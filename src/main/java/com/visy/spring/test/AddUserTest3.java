package com.visy.spring.test;

import com.visy.spring.core.BeanFactory;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

/**
 *  通过xml配置实现自动注入属性值-set
 */
public class AddUserTest3 {
    public static void main(String[] args) {

        //通过Bean工厂获取对象
        UserService userService = (UserService) BeanFactory.getBean("userService");

        //创建要保存的用户
        User user = new User(3,"王五","003");

        userService.add2(user);//添加用户
    }
}
