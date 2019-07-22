package com.visy.spring.core;

import com.visy.spring.annotation.Component;
import com.visy.spring.util.PackageScanner;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注解处理器
 */
public class AnnotationProcessor {

    public static Map<String,Object> parse(String[] packages, boolean bySetter){
        Map<String,Object> container = new HashMap<String, Object>();//盛放所有bean的容器

        List<String> classPaths = PackageScanner.getClassesByPackages(packages);
        try{
            for(String classPath: classPaths){
                Class<?> clazz = Class.forName(classPath);
                componentProcessor(clazz, container);
            }
            for(Map.Entry<String,Object> entry: container.entrySet()){
                resourceProcessor(entry.getValue(),container, bySetter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return container;
    }

    public static void componentProcessor(Class<?> clazz, Map<String,Object> container){
        if(clazz.isAnnotationPresent(Component.class)){
            Component $component = clazz.getAnnotation(Component.class);
            String beanName = $component.value();
            if(isBlank(beanName)){
                String className = clazz.getSimpleName();//默认使用类名，首字母小写
                beanName = className.substring(0,1).toLowerCase();
                if(className.length() > 1){
                    beanName = beanName + className.substring(1);
                }
            }
            try{
                container.put(beanName, clazz.newInstance());
            }catch (Exception e){
                System.out.println("实例化bean失败："+clazz);
            }
        }
    }

    public static void resourceProcessor(Object instance, Map<String,Object> container, boolean bySetter){
        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field: fields){
            String fieldName = field.getName();
            if(field.isAnnotationPresent(Resource.class)){
                Resource $resource = field.getAnnotation(Resource.class);
                String refName = $resource.name();
                if(isBlank(refName)){
                    refName = fieldName;//默认使用属性名
                }
                Object refBean = container.get(refName);
                if(refBean != null){
                    if(bySetter){
                        injectBySetMethod(clazz,instance,fieldName,refBean);
                    }else{
                        injectByField(field,instance,refBean);
                    }
                }
            }
        }
    }

    public static void injectBySetMethod(Class<?> clazz, Object instance, String fieldName, Object refBean){
        String setName = "set"+fieldName.substring(0,1).toUpperCase();
        if(fieldName.length() > 1){
            setName = setName + fieldName.substring(1);
        }
        try{
            Method setter = clazz.getDeclaredMethod(setName,refBean.getClass().getInterfaces());
            setter.setAccessible(true);
            setter.invoke(instance, refBean);
        }catch (Exception e){
            System.out.println("注入属性出错(setter)："+ clazz+"."+fieldName);
        }
    }

    public static void injectByField(Field field, Object instance,Object refBean){
        field.setAccessible(true);
        try{
            field.set(instance,refBean);
        }catch (Exception e){
            System.out.println("注入属性出错(field)："+ instance.getClass()+"."+field.getName());
        }
    }



    public static boolean isBlank(String s){
        return s==null || "".equals((s+"").trim());
    }
}
