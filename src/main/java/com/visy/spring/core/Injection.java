package com.visy.spring.core;

import com.visy.spring.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Injection {

    public static void bySetter(Object target, String fieldName, Object refBean){
        Class<?> clazz = target.getClass();
        String setName = "set"+ StringUtil.nameFormat(fieldName, true);
        String fullName = clazz+"."+setName;
        try{
            Method setter = clazz.getDeclaredMethod(setName,refBean.getClass().getInterfaces());//不严谨的写法
            setter.setAccessible(true);
            setter.invoke(target, refBean);
        }catch (NoSuchMethodException e){
            System.out.println("找不到set方法："+fullName);
        }catch (IllegalAccessException e){
            System.out.println("set方法调用安全权限异常："+fullName);
        }catch (InvocationTargetException e){
            System.out.println("set调用目标异常："+fullName);
        }
    }

    public static void byField(Object target, Field field, Object refBean){
        String fullName  = target.getClass()+"."+field.getName();
        try{
            field.setAccessible(true);
            field.set(target,refBean);
        }catch (Exception e){
            System.out.println("注入属性出错(field)："+fullName);
        }
    }
}
