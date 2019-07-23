package com.visy.spring.test;

import com.visy.spring.annotation.Component;
import com.visy.spring.core.BeanFactory;
import com.visy.spring.model.User;
import com.visy.spring.service.UserService;

import javax.annotation.Resource;

/**
 * 通过annotation配置实现自动注入属性值-set（2）
 */
@Component
public class AddUserTest5 {
    @Resource
    private UserService userService;

    public static void main(String[] args) {
        AddUserTest5 test5 = (AddUserTest5)BeanFactory.getBean3("addUserTest5");
        test5.add();
    }

    public void add(){
        //创建要保存的用户
        User user = new User(4,"陈六","004");
        userService.add3(user);//添加用户
    }

    //setter 注入使用
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
