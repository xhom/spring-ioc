package com.visy.spring.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    //获取指定包下的所有类路径
    public static List<String> getClassesByPackages(String[] packages) {
        List<String> allClassPathList = new ArrayList<String>();

        //获取包的文件路径
        String basePath = ClassLoader.getSystemResource("").getPath();
        for(String pkgPath: packages){
            String filePath = basePath + pkgPath.replace(".", "/");
            //获取包下所有类路径
            List<String> classPathList = new ArrayList<String>();
            getClassPaths(filePath, classPathList);
            allClassPathList.addAll(classPathList);
        }

        return allClassPathList;
    }

    private static void getClassPaths(String rootPath, List<String> result){
        File rootFile = new File(rootPath);
        File[] children = rootFile.listFiles();
        if(children==null){
            result.add(classPathPickUp(rootFile.getPath()));
            return;
        }
        for(File child: children){
            String childPath = child.getPath();
            if(child.isDirectory()){
                getClassPaths(childPath, result);
            }else{
                result.add(classPathPickUp(childPath));
            }
        }
    }

    //从文件路径提取类路径
    private static String classPathPickUp(String filePath){
        if(filePath!=null && !"".equals(filePath)){
            int start = filePath.indexOf("classes");
            int end = filePath.indexOf(".class");
            String classPath = filePath.substring(start,end).replace("\\",".");
            return classPath.replace("classes.","");
        }
        return filePath;
    }
}
