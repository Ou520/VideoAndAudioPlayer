package com.example.textthread.JsonAsyncTask.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//写一个类专门处理网络存来的Json数据，方便在别的地方随时调用
public class HttpJsonUtils {
    //写一个静态方法处理json数据
    public static String getJsonData(String address)
    {
        StringBuilder builder = new StringBuilder();//定义一个可变的字符序列（StringBuilder 是一个可变的字符序列）
        try{//做异常处理
            URL myUrl = new URL(address);//定义URL对象，并把url赋值个myUrl
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();//打开链接（url），返回HttpURLConnection对象赋值给connection
            //InputStreamReader类是从字节流到字符流的桥接器：它使用指定的字符集读取字节并将它们解码为字符。 它使用的字符集可以通过名称指定，也可以明确指定，或者可以接受平台的默认字符集。每次调用一个InputStreamReader的read（）方法都可能导致从底层字节输入流中读取一个或多个字节。 为了实现字节到字符的有效转换，可以从基础流中提取比满足当前读取操作所需的更多字节。为了获得最高效率，请考虑在BufferedReader中包装InputStreamReader
            InputStreamReader isReader = new InputStreamReader(connection.getInputStream());//connection.getInputStream():定义输入流，并赋值给isReader
            BufferedReader buffReader = new BufferedReader(isReader);//定义BufferedReader变量,为其他字符输入流添加一些缓冲功能
            String line = "";//定义字符串line，让他每次读取一行数据
            while((line = buffReader.readLine())!=null)//判断读取文件是否到文件尾，如果不是在读下一行，直到文件尾；readLine()：读取一行数据
            {
                builder.append(line);//把一行的数据写入可变的字符序列builder里；append():追加
            }
        }catch (Exception e)
        {
            Log.v("task",e.getMessage());//打印日志
        }
        return builder.toString();
    }
}
