package com.example.textthread.ImdAsynTaak.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
//通过链接下载网络上的图片并进行处理产生位图对象（Bitmap）
public class HttpBitmapUtils{
    public Bitmap getBitmap(String params)
    {
        Bitmap bitmap = null;//定义位图对象
        OutputStream os = null;//定义输出流对象
        InputStream is =null;//定义输入流对象
        try {
            URL myUrl =new URL(params);//定义URL对象，并给地址，也可以给网络协议和端口号
            HttpURLConnection connection=(HttpURLConnection) myUrl.openConnection();//打开链接（url），返回HttpURLConnection对象赋值给connection
            is =connection.getInputStream();//定义输入流，并赋值给is
            ByteArrayOutputStream bos =new ByteArrayOutputStream();
            os = new BufferedOutputStream(bos);
            int total=connection.getContentLength();//获取文件的总长度
            int current = 0;//
            int read= 0;//读取了多少
            byte[] data=new byte[1024];//
            while ((read =is.read(data)) !=-1)
            {
                os.write(data,0,read);//把读取的数据存进输出流里
                current = current + read;
                int progress =(int) ((float)current/total*100);//计算当前的进度，并赋值给progress

                //问老师，！！！！！！！！！！！！！！！！！！！！！！！
//                publishProgress(progress);//用发布信息来触发进度变化进而执行onProgressUpdate方法，并可以给onProgressUpdate传递参数，参数的类型和onProgressUpdate的参数类型有关
            }
            os.flush();
            byte[] bitmapData = bos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(is !=null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
