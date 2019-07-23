package com.visy.spring.core;

import com.visy.spring.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.*;

public class ClassPathXmlApplicationContext {

    public static Map<String, Object> parse(String path){

        Map<String,Object> container = new HashMap<String, Object>();//盛放所有bean的容器
        Map<String, List<String>> refPropsMap = new HashMap<String, List<String>>();//用于保存待注入属性的map

        parseBeans(path, container, refPropsMap);

        inject(container, refPropsMap);

        return container;
    }

    private static void parseBeans(String path, Map<String,Object> container, Map<String, List<String>> refPropsMap){
        //读取配置文件并获得根节点root
        SAXReader reader = new SAXReader();
        Document doc = null;
        try{
            doc = reader.read(path);
        }catch (DocumentException e){
            System.out.println("文档读取异常："+ path);
        }
        if(doc == null){
            System.out.println("文档读取结果为空: "+ path);
            return;
        }

        Element root = doc.getRootElement();
        Iterator<Element>  beans = root.elementIterator();

        while(beans.hasNext()){//遍历所有的bean
            Element beanEle = beans.next();

            //读取bean的名称和class路径
            String name = beanEle.attributeValue("id");
            if(name == null){ //id读取不到的时候读取name
                name = beanEle.attributeValue("name");
            }
            String classPath = beanEle.attributeValue("class");

            //查找bean下所有property
            Iterator<Element> properties = beanEle.elementIterator("property");
            List<String> refPropList = null;//用于保存property的信息
            while(properties.hasNext()){
                Element property = properties.next();

                String fieldName = property.attributeValue("name");
                String refName = property.attributeValue("ref");

                if(StringUtil.isBlank(fieldName) || StringUtil.isBlank(refName)){
                    continue; //没有name或ref的属性无需注入
                }

                if(refPropList == null){//用到的时候才创建列表
                    refPropList = new ArrayList<String>();
                }
                refPropList.add(fieldName+","+refName);// name,ref
            }

            try{
                Object bean = Class.forName(classPath).newInstance();
                container.put(name, bean);
                refPropsMap.put(name,refPropList);
            }catch (ClassNotFoundException e){
                System.out.println("找不到Class："+ classPath);
            }catch (InstantiationException e){
                System.out.println("实例化bean失败："+ classPath);
            }catch (IllegalAccessException e){
                System.out.println("实例化bean安全权限异常："+ classPath);
            }
        }
    }

    //注入 setter
    private static void inject(Map<String,Object> container, Map<String, List<String>> refPropsMap){
        //注入实现 set注入 （应该在所有bean创建完成后注入）
        for(Map.Entry<String,Object> entry: container.entrySet()){
            List<String> refProps = refPropsMap.get(entry.getKey());
            if(refProps == null){
                continue; //没有待注入的属性
            }
            Object target = entry.getValue();//注入目标对象

            for(String refProp: refProps){//依次注入
                String[] refPropAttrs = refProp.split(",");
                String fieldName = refPropAttrs[0];//取得属性名
                String refName = refPropAttrs[1];//取得ref
                Object refBean = container.get(refName);

                if(refBean != null){
                    Injection.bySetter(target,fieldName,refBean);
                }
            }
        }

    }
}
