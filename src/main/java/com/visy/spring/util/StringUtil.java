package com.visy.spring.util;

public class StringUtil {

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
