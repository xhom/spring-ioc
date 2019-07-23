package com.visy.spring.aop.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader{
    private File dir;

    //初始化字节码文件
    public MyClassLoader(String path) {
        this.dir = new File(path);
    }

    //本方法就是去加载对应的字节码文件
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //如果文件路径可用
        if (dir != null) {
            File clazzFile = new File(dir, name + ".class");
            //如果字节码文件存在
            if (clazzFile.exists()) {
                //把字节码文件加载到VM
                try {
                    //文件流对接class文件
                    FileInputStream inputStream = new FileInputStream(clazzFile);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    //将class文件读取到buffer中
                    while ((len = inputStream.read(buffer)) != -1) {
                        //将buffer中的内容读取到baos中的buffer
                        baos.write(buffer, 0, len);
                    }
                    //将buffer中的字节读到内存加载为class
                    return defineClass("proxy." + name, baos.toByteArray(), 0, baos.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.findClass(name);

        // 也可以使用URLClassLoader 加载，如下所示:
        // URL[] urls = new URL[]{new URL(path)};
        // URLClassLoader urlClassLoader = new URLClassLoader(urls);
        // Class cls = urlClassLoader.loadClass("proxy.$Proxy0");

    }
}
