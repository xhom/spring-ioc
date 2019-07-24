package com.visy.spring.aop.my;

import com.visy.spring.util.StringUtil;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *  1. 第一次执行代理类加载出错：ClassNotFoundException
 *  2.interfaces间有继承关系的情况没处理
 */
public class MyProxy {
    private static final String RT = "\r\n";
    private static final String SP = File.separator;
    private static final String PROXY_NAME = "$Proxy0";

    /**
     * 生成代理类源码
     * @param interfaces 接口Class
     * @return
     */
    private static String getProxySrc(Class<?>[] interfaces){
        if(interfaces==null ||interfaces.length==0){
            System.out.println("没有可代理的interfaces...");
            return null;
        }
        System.out.println("开始生成代理类...");
        String pkg = getPkg();
        String importItfs = "";
        String implementsItfs = "";
        String itfClassesDef = "";
        int index = 0;
        for(Class<?> itfClass : interfaces){
            String itfPath = itfClass.getName();
            String itfName = itfClass.getSimpleName();
            importItfs += "import "+itfPath+";"+RT;
            implementsItfs += itfName+",";
            itfClassesDef += "   private Class<?> itfClass"+index+" = "+itfName+".class;"+RT;
            index ++;
        }
        implementsItfs = StringUtil.deleteLastChar(implementsItfs);

        String proxyClass =
                "package "+pkg+";"+RT+RT+
                "import java.lang.reflect.Method;"+RT+RT+
                 importItfs+RT+
                "public class "+PROXY_NAME+" implements "+implementsItfs+"{"+RT+
                "   private MyInvocationHandler handler;"+RT+
                    itfClassesDef+RT+
                "   public "+PROXY_NAME+"(MyInvocationHandler handler){"+RT+
                "       this.handler = handler;"+RT+
                "   }"+RT+RT+
                    getMethodsSrc(interfaces)+
                "}"+RT;
        System.out.println("代理类生成完毕");
        return proxyClass;
    }

    private static String getMethodsSrc(Class<?>[] interfaces){
        String allMethodsStr = "";
        int itf_index = 0;
        for(Class<?> itfClass: interfaces){
            Method[] methods = itfClass.getMethods();
            String methodsStr = "";
            for(Method method: methods){
                String methodName = method.getName();
                String returnType = method.getReturnType().getName();

                String parameterTypes = "";
                String parameters = "";
                String argSeq = "";
                boolean hasParameters = false;
                int index = 0;
                for(Class<?> parameterType: method.getParameterTypes()){
                    String type = parameterType.getName();
                    parameterTypes += ","+type+".class";
                    parameters += type+" arg"+index+",";
                    argSeq += "arg"+index+",";
                    hasParameters = true;
                    index ++;
                }
                if(hasParameters){
                    parameters = StringUtil.deleteLastChar(parameters);
                    argSeq = StringUtil.deleteLastChar(argSeq);
                }

                methodsStr +=
                        "   @Override"+RT+
                                "   public "+returnType+" "+methodName+"("+parameters+"){"+RT+
                                "       try{"+RT+
                                "           Method method = this.itfClass"+itf_index+".getMethod(\""+methodName+"\""+parameterTypes+");"+RT+
                                returnStr(returnType,argSeq)+RT+
                                "       }catch(Throwable e){"+RT+
                                "           System.out.println(\"代理方法获取失败\");"+RT+
                                "       }"+RT+
                                returnStr2(returnType)+
                                "   }"+RT;
            }
            allMethodsStr += methodsStr;
            itf_index ++;
        }

        return allMethodsStr;
    }

    public static String returnStr(String returnType, String argSeq){
        String args = "".equals(argSeq) ? "null" : "new Object[]{"+argSeq+"}";
        if("void".equals(returnType)){
            return "           this.handler.invoke(this, method, "+args+");";
        }else{
            return "           return ("+returnType+")this.handler.invoke(this, method, "+args+");";
        }
    }
    public static String returnStr2(String returnType){
        if(!"void".equals(returnType)){
            String val = "";
            if("byte".equals(returnType) || "short".equals(returnType) || "int".equals(returnType)){
                val = "0";
            }else if("long".equals(returnType)){
                val = "0L";
            }else if("float".equals(returnType)){
                val = "0.0f";
            }else if("double".equals(returnType)){
                val = "0.0d";
            }else if("char".equals(returnType)){
                val = "'\\u0000'";
            }else if("boolean".equals(returnType)){
                val = "false";
            }else{
                val = "null";
            }
            return "       return "+val+";"+RT;
        }
        return "";
    }

    public static String writeProxy(String proxyClass, String fileName){
        System.out.println("开始写入代理类文件...");
        String basePath = ClassLoader.getSystemResource("").getPath();
        String suffixPath = MyProxy.class.getName().replace(".", SP);
        suffixPath = suffixPath.replace(SP+"MyProxy",SP);
        String filePath = basePath.replace("/",SP) + suffixPath;
        filePath = filePath.replace(SP+"target"+SP+"classes"+SP,SP+"src"+SP+"main"+SP+"java"+SP);
        filePath = filePath+fileName+".java";

        File f = new File(filePath);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(proxyClass);
            fw.flush();
            fw.close();
            System.out.println("代理类文件写入成功："+filePath);
        } catch (IOException e) {
            System.out.println("代理类文件写入失败"+filePath);
            return null;
        }
        return f.getPath();
    }


    private static void compileJavaFile(String filePath) {
        System.out.println("开始编译代理类...");
        try{
            //获取系统当前java编译器
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            //获得文件管理器
            StandardJavaFileManager manager = compiler.getStandardFileManager(null,null, null);
            Iterable<? extends JavaFileObject> javaFiles = manager.getJavaFileObjects(filePath);
            //编译任务
            JavaCompiler.CompilationTask task = compiler.getTask(null,manager,null,null,null,javaFiles);
            task.call();//开始编译，执行完毕后当前目录生成.class文件
            manager.close();//关闭文件管理器
            System.out.println("代理类编译完毕："+filePath.replace(".java",".class"));
        }catch (IOException e){
            System.out.println("代理类编译失败："+filePath.replace(".java",".class"));
        }
    }

    private static Class<?> loadClassToJVM(String filePath){
        System.out.println("开始加载代理类class到JVM...");
        try{
            //class url指定
            String proxySpec = filePath.substring(0,filePath.indexOf(SP+"main"));//取到src目录
            File file = new File(proxySpec);
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            //去指定路径寻找class文件
            URLClassLoader urlClassLoader = new URLClassLoader(urls);
            Class<?> proxyClass = urlClassLoader.loadClass(getPkg()+"."+PROXY_NAME);//完整类名（含包信息）
            System.out.println("代理类class已加载到JVM");
            return proxyClass;
        }catch (Exception e){
            System.out.println("代理类class加载到JVM失败");
            e.printStackTrace();
        }
        return null;
    }

    public static String getPkg(){
        return MyProxy.class.getPackage().getName();
    }

    public static Object newProxyInstance(Class<?>[] interfaces,MyInvocationHandler handler){
        //1.生成代理类源码
        String proxyClass = getProxySrc(interfaces);
        //2.将代理类源码写入.java文件
        String filePath = writeProxy(proxyClass,PROXY_NAME);
        //3.编译写入的java文件
        compileJavaFile(filePath);
        //4.将编译后的class文件加载到JVM
        Class<?> clazz =  loadClassToJVM(filePath);
        try{
            Constructor<?> constructor = clazz.getConstructor(MyInvocationHandler.class);
            return constructor.newInstance(handler);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("代理类实例化失败");
        }
        return null;
    }
}
