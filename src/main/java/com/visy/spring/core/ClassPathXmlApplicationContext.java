package com.visy.spring.core;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Method;
import java.util.*;

public class ClassPathXmlApplicationContext {

    public static Map<String, Object> parse(String path){

        Map<String,Object> container = new HashMap<String, Object>();//盛放所有bean的容器
        Map<String, List<String>> refPropsMap = new HashMap<String, List<String>>();//用于保存待注入属性的map

        try {
            //读取配置文件并获得根节点root
            SAXReader reader = new SAXReader();
            Document doc = reader.read(path);
            Element root = doc.getRootElement();

            Iterator<Element>  beans = root.elementIterator();
            while(beans.hasNext()){//遍历所有的bean
                Element bean = beans.next();

                //读取bean的名称和class路径
                String name = bean.attributeValue("id");
                if(name == null){ //id读取不到的时候读取name
                    name = bean.attributeValue("name");
                }
                String classPath = bean.attributeValue("class");

                //查找bean下所有property
                Iterator<Element> properties = bean.elementIterator("property");
                List<String> refPropList = null;//用于保存property的信息
                while(properties.hasNext()){
                    Element property = properties.next();
                    String ref = property.attributeValue("ref");
                    if(ref == null){
                        continue; //没有ref的属性无需注入
                    }
                    String propName = property.attributeValue("name");
                    if(refPropList == null){//用到的时候才创建列表
                        refPropList = new ArrayList<String>();
                    }
                    refPropList.add(propName+","+ref);// name,ref
                }

                refPropsMap.put(name,refPropList);
                container.put(name, Class.forName(classPath).newInstance());
            }

            //注入实现 set注入 （应该在所有bean创建完成后注入）
            for(Map.Entry<String,Object> entry: container.entrySet()){
                List<String> refProps = refPropsMap.get(entry.getKey());
                if(refProps == null){
                    continue; //没有待注入的属性
                }
                Object instance = entry.getValue();//注入目标对象
                Class<?> clazz = instance.getClass();

                for(String refProp: refProps){//依次注入
                    String[] refPropAttrs = refProp.split(",");
                    String name = refPropAttrs[0];//取得属性名
                    String ref = refPropAttrs[1];//取得ref

                    Object refBean = container.get(ref);
                    if(refBean == null){
                        continue; //待注入对象不存在
                    }

                    String setName = "set"+name.substring(0,1).toUpperCase()+name.substring(1);
                    Method setter = clazz.getDeclaredMethod(setName, refBean.getClass().getInterfaces());//不严谨写法
                    setter.setAccessible(true);
                    setter.invoke(instance, refBean);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return container;
    }
}
