package com.visy.spring.jython;

import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 执行py文件
 */
public class ExecutePyFile {
    public static void main(String[] args) throws Exception{
        //PythonInterpreter interpreter = new PythonInterpreter();
        //interpreter.exec("print 'hello python!'");
        //interpreter.execfile("./src/main/java/com/visy/spring/jython/test.py");

        Runtime runtime = Runtime.getRuntime();
        File file = new File("D:\\developer\\pyCharm\\workspace\\scrapy_test");
        runtime.exec("scrapy crawl cqut_spider",null,file );
        //printReturn(process);
    }
    public static void printReturn(Process process) throws Exception{
        BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
        String line;
        while((line=reader.readLine()) != null){
            System.out.println(line);
        }
    }

}
