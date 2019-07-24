package com.visy.spring.util;

public class StringUtil {

    public static boolean isBlank(String s){
        return s==null || "".equals((s+"").trim());
    }

    public static String nameFormat(String name, boolean isFirstUp){
        if(isBlank(name)){
            return name;
        }
        String newName = name.substring(0,1);
        newName = isFirstUp ? newName.toUpperCase() : newName.toLowerCase();
        if(name.length() > 1){
            newName = newName + name.substring(1);
        }
        return newName;
    }

    public static String deleteLastChar(String s){
        if(isBlank(s)){
           return s;
        }
        return s.substring(0,s.length()-1);
    }
}
