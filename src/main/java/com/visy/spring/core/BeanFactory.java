package com.visy.spring.core;

import java.util.Map;

public class BeanFactory {
    private static final String[] SCAN_PACKAGES = {
            "com.visy.spring.service",
            "com.visy.spring.dao"
    };
    private static final String[] SCAN_PACKAGES2 = {
            "com.visy.spring.test",
            "com.visy.spring.service",
            "com.visy.spring.dao"
    };
    private static Map<String, Object> container1;
    private static Map<String, Object> container2;
    private static Map<String, Object> container3;
    private static Map<String, Object> container4;

    /**
     * 通过xml初始化bean并实现注入
     * @param name
     * @return
     */
    public static Object getBean(String name){
        if(container1 == null){
            container1 = ClassPathXmlApplicationContext.parse("src\\beans.xml");
        }
        return container1.get(name);
    }

    /**
     * 通过注解初始化bean并实现注入 set注入
     * @param name
     * @return
     */
    public static Object getBean2(String name){
        if(container2 == null){
            container2 = AnnotationProcessor.parse(SCAN_PACKAGES, true);
        }
        return container2.get(name);
    }

    public static Object  getBean3(String name){
        if(container3 == null){
            container3 = AnnotationProcessor.parse(SCAN_PACKAGES2,true);
        }
        return container3.get(name);
    }
    /**
     * 通过注解初始化bean并实现注入 field注入
     * @param name
     * @return
     */
    public static Object  getBean4(String name){
        if(container4 == null){
            container4 = AnnotationProcessor.parse(SCAN_PACKAGES2,false);
        }
        return container4.get(name);
    }
}
