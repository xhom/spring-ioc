package com.visy.spring.test;

import com.visy.spring.annotation.Component;
import com.visy.spring.core.BeanFactory;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

import javax.annotation.Resource;

/**
 * 通过annotation配置实现自动注入属性值-属性注入（不用写set方法）
 */
@Component
public class AddUserTest6 {
    @Resource
    private UserService userService;

    public static void main(String[] args) {
        AddUserTest6 test5 = (AddUserTest6) BeanFactory.getBean4("addUserTest6");
        test5.add();
    }

    public void add(){
        User user = new User(5,"小七","005");
        userService.add4(user);//添加用户
    }
}
