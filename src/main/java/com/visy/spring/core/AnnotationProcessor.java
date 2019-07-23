package com.visy.spring.core;

import com.visy.spring.annotation.Component;
import com.visy.spring.util.PackageScanner;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        //实例化容器中的所有bean
        for(String classPath: classPaths){
            componentProcessor(classPath, container);
        }
        //为bean注入属性值
        for(Map.Entry<String,Object> entry: container.entrySet()){
            resourceProcessor(entry.getValue(), container, bySetter);
        }
        return container;
    }

    //解析@Component注解
    public static void componentProcessor(String classPath, Map<String,Object> container){
        try{
            Class<?> clazz = Class.forName(classPath);
            if(clazz.isAnnotationPresent(Component.class)){
                Component $component = clazz.getAnnotation(Component.class);
                String beanName = $component.value();
                if(isBlank(beanName)){ //默认使用类名，首字母小写
                    beanName = nameFormat(clazz.getSimpleName(), false);
                }
                Object bean = clazz.newInstance();
                container.put(beanName, bean);
            }
        }catch (ClassNotFoundException e){
            System.out.println("找不到Class："+ classPath);
        }catch (InstantiationException e){
            System.out.println("实例化bean失败："+ classPath);
        }catch (IllegalAccessException e){
            System.out.println("实例化bean安全权限异常："+ classPath);
        }
    }

    //解析@Resource注解
    public static void resourceProcessor(Object instance, Map<String,Object> container, boolean bySetter){
        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field: fields){
            if(field.isAnnotationPresent(Resource.class)){
                String fieldName = field.getName();
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
        String setName = "set"+ nameFormat(fieldName, true);
        try{
            Method setter = clazz.getDeclaredMethod(setName,refBean.getClass().getInterfaces());//不严谨的写法
            setter.setAccessible(true);
            setter.invoke(instance, refBean);
        }catch (NoSuchMethodException e){
            System.out.println("找不到set方法："+clazz+"."+setName);
        }catch (IllegalAccessException e){
            System.out.println("set方法调用安全权限异常："+clazz+"."+setName);
        }catch (InvocationTargetException e){
            System.out.println("set调用目标异常："+clazz+"."+setName);
        }
    }

    public static void injectByField(Field field, Object instance,Object refBean){
        try{
            field.setAccessible(true);
            field.set(instance,refBean);
        }catch (Exception e){
            System.out.println("注入属性出错(field)："+ instance.getClass()+"."+field.getName());
        }
    }



    public static boolean isBlank(String s){
        return s==null || "".equals((s+"").trim());
    }

    public static String nameFormat(String name, boolean isFirstUp){
        if(!isBlank(name)){
            String newName = name.substring(0,1);
            newName = isFirstUp ? newName.toUpperCase() : newName.toLowerCase();
            if(name.length() > 1){
                newName = newName + name.substring(1);
            }
            return newName;
        }
        return name;
    }
}
