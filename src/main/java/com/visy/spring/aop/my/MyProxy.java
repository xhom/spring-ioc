package com.visy.spring.aop.my;

import com.visy.spring.aop.Developer;
import com.visy.spring.aop.People;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyProxy {
    private static String rt = "\r\n";

    private static String get$Proxy0(Class<?> interfaceClass){
        String proxyClass =
                "package com.visy.spring.aop.my;"+rt+
                "import java.lang.reflect.Method;"+rt+
                "public class $Proxy0 implements "+interfaceClass.getName()+"{"+rt+
                "   private MyInvocationHandler handler;"+rt+
                "   public $Proxy0(MyInvocationHandler handler){"+rt+
                "       this.handler = handler;"+rt+
                "   }"+rt+
                getMethods(interfaceClass)+
                "}"+rt;
        return proxyClass;
    }

    public static String getMethods(Class<?> interfaceClass){
        Method[] methods = interfaceClass.getMethods();
        String methodsStr = "";
        for(Method m: methods){
            String mName = m.getName();
            methodsStr +=
                    "   public void "+mName+"() throws Throwable {"+rt+
                    "       Method md = "+interfaceClass.getName()+".class.getMethod(\""+mName+"\");"+rt+
                    "       this.handler.invoke(this,md,null);"+rt+
                    "   }"+rt;
        }

        return methodsStr;
    }

    public static void write$Proxy0(String proxyClass, String path){
        File f = new File(path);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(proxyClass);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void compileJavaFile(String fileName) {
        //获取系统当前java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //获得文件管理器
        StandardJavaFileManager manager = compiler.getStandardFileManager(null,null, null);
        Iterable<? extends JavaFileObject> javaFiels = manager.getJavaFileObjects(fileName);
        //编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null,manager,null,null,null,javaFiels);
        //开始编译，执行完毕后当前目录生成.class文件
        task.call();
        try{
            manager.close();//关闭文件管理器
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Object loadClassToJVM(MyInvocationHandler handler){
        try{
            //使用自定义类加载器
            MyClassLoader myLoader = new MyClassLoader("D:\\developer\\idea\\workspace\\spring-demo\\src\\main\\java\\com\\visy\\spring\\aop\\my");
            Class<?> $proxy0 = myLoader.findClass("$Proxy0");
            Constructor<?> constructor = $proxy0.getConstructor(MyInvocationHandler.class);
            return constructor.newInstance(handler);
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object newProxyInstance(ClassLoader loader,Class<?> interfaces,MyInvocationHandler handler) {
        //FIXME 该路径需要自己定义,我们定义为proxy/temp的绝对路径，这样代理类就会在该文件夹下生成
        String path = "D:\\developer\\idea\\workspace\\spring-demo\\src\\main\\java\\com\\visy\\spring\\aop\\my\\$Proxy0.java";

        //1、用已知的接口，遍历里面的方法，以字符串的形式拼凑出内存里的代理类（动态代理类与被代理类实现同一接口在此体现）
        String proxyClass = get$Proxy0(interfaces);
        //2、将代理类的写到本地java文件
        write$Proxy0(proxyClass, path);
        //3、编译java文件
        compileJavaFile(path);
        //4、将对应编译出来的字节码加载到JVM内存
        return loadClassToJVM(handler);
    }

}
