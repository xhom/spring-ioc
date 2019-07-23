package com.visy.spring.core;

import com.visy.spring.annotation.Component;
import com.visy.spring.util.PackageScanner;
import com.visy.spring.util.StringUtil;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注解处理器
 */
public class AnnotationProcessor {

    //解析入口
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
    private static void componentProcessor(String classPath, Map<String,Object> container){
        try{
            Class<?> clazz = Class.forName(classPath);
            if(clazz.isAnnotationPresent(Component.class)){
                Component $component = clazz.getAnnotation(Component.class);
                String beanName = $component.value();
                if(StringUtil.isBlank(beanName)){ //默认使用类名，首字母小写
                    beanName = StringUtil.nameFormat(clazz.getSimpleName(), false);
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
    private static void resourceProcessor(Object target, Map<String,Object> container, boolean bySetter){
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field: fields){
            if(field.isAnnotationPresent(Resource.class)){
                String fieldName = field.getName();
                Resource $resource = field.getAnnotation(Resource.class);
                String refName = $resource.name();
                if(StringUtil.isBlank(refName)){
                    refName = fieldName;//默认使用属性名
                }
                Object refBean = container.get(refName);
                if(refBean != null){
                    if(bySetter){
                        Injection.bySetter(target,fieldName,refBean);
                    }else{
                        Injection.byField(target,field,refBean);
                    }
                }
            }
        }
    }
}
