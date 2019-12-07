package com.example.textthread.ImdAsynTaak;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.textthread.ImdAsynTaak.Utils.HttpBitmapUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
﻿AsyncTask内的各个方法调用顺序：

        一、首先，用户调用execute方法，启动AsyncTask 。

        二、然后在execute方法中：

        1、首先调用onPreExecute方法，执行初始化操作。

        2、然后从线程池中取出若干个空闲的线程，并使用该线程调用doInBackground方法，执行耗时的操作，如文件下载等。提示：调用execute方法时设置的参数会被直接传递给doInBackground方法。

        3、当doInBackground方法执行完毕后，onPostExecute方法将被调用。onPostExecute方法的参数就是doInBackground方法的返回值。

        4、若doInBackground方法中途被终止，则同样会调用onPostExecute方法，但是方法的参数却为null 。

        5、若想更新UI控件，则可以在doInBackground方法中调用publishProgress方法向主线程中的Handler发送消息，Handler接到消息后会转调用onProgressUpdate方法来更新UI。提示：调用publishProgress方法时设置的参数将被传递给onProgressUpdate方法。

        在上面的范例中，各个方法的参数、返回值都是Object类型的，这对于严格控制程序有很大负面的影响。但是事实上，AsyncTask类是有泛型的。

        注：AsyncTask<Params, Progress, Result> 其中：

        1、Params：用于设置execute和doInBackground方法的参数的数据类型。

        2、Progress：用于设置onProgressUpdate和publishProgress方法的参数的数据类型。

        3、Result：用于设置onPostExecute方法的参数的数据类型和doInBackground方法的返回值类型。

        execute(params)--->onPreExecute()------>result doInBackground(params)-------->onPostExecute(result///null) ----->publishProgress(Progress)---->mainUI Handler ---->onProgressUpdate(Progress）

*/



public class ImgAsyncTask extends AsyncTask<String,Integer, Bitmap> {
    ImageView img;
    ProgressBar pbImgLoad;
    TextView tvImgLoad;
    Context context;
//        x写一个构造方法，并赋值ImageView变量
        public ImgAsyncTask(ImageView img,ProgressBar pbImgLoad,TextView tvImgLoad)
        {
            this.img=img;
            this.pbImgLoad=pbImgLoad;
            this.tvImgLoad=tvImgLoad;
        }

    @Override
    //1.初始化工作在这个方法里
    protected void onPreExecute() {
        super.onPreExecute();
        pbImgLoad.setVisibility(View.VISIBLE);//把pbImgLoad显示出来
        tvImgLoad.setVisibility(View.VISIBLE);
    }

    @Override
    //2.网络的数据请求在这个方法里
    protected Bitmap doInBackground(String... params) {
            //方法一

        Bitmap bitmap = null;//定义位图对象
        OutputStream os = null;//定义输出流对象
        InputStream is =null;//定义输入流对象
        String url=params[0];//把params数组的第一个值赋值给url
        try {
            URL myUrl =new URL(url);//定义URL对象，并给地址，也可以给网络协议和端口号
            HttpURLConnection connection=(HttpURLConnection) myUrl.openConnection();//打开链接（url），返回HttpURLConnection对象赋值给connection
            is =connection.getInputStream();//定义输入流，并赋值给is
            ByteArrayOutputStream bos =new ByteArrayOutputStream();//定义一个字节输出流;此类实现一个字节输出流、其中数据被写入到字节数组中， 缓冲区在数据写入时会自动增长，关闭该流无效，关闭此流后调用方法不会有异常
            os = new BufferedOutputStream(bos);//把数据一部分一部分的读进字节数组里
            int total=connection.getContentLength();//获取文件的总长度
            int current = 0;//当前已经下载了多少
            int read= 0;//读取了多少
            byte[] data=new byte[1024];//把读取的数据放进data的字节数组里（字节数组的小可以自己定）
            while ((read =is.read(data)) !=-1)//判断读取的数据发送是文件的结尾，如果不是就一直执行循环直文件结尾
            {
                os.write(data,0,read);//把data里的数据写进输出流里（从0开始到read）
                //计算进度
                current = current + read;//当前读到了多少
                int progress =(int) ((float)current/total*100);//计算当前的进度（把当前的进度/总的长度*100），并赋值给progress
                publishProgress(progress);//用发布信息来触发进度变化进而执行onProgressUpdate方法，并可以给onProgressUpdate传递参数，参数的类型和onProgressUpdate的参数类型有关
            }
            os.flush();//刷新一下数据
            byte[] bitmapData = bos.toByteArray();//把字节输出流转换为字节数组赋值给bitmapData
            bitmap = BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length);//用位图的工厂类，对整个字节数组进行解码（看一下里面的其他类）
//            bitmap = BitmapFactory.decodeStream(is);//把输入流转换为位图，并赋值给bitmap
        } catch (Exception e) {
            Log.v("异常类型：",e.getMessage());
        }finally {
            if(is !=null)
            {
                try {
                    is.close();//关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

         //方法二
//         Bitmap bitmap=new HttpBitmapUtils().getBitmap(params[0]);//定义一个位图对象并赋值（调用HttpBitmapUtils类给bitmap赋值）
        return bitmap;
    }

    @Override
    //数更新在这个方法里
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pbImgLoad.setProgress(values[0]);
        tvImgLoad.setText(values[0] +"%");
    }

    @Override
    //网络请求完成
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        img.setImageBitmap(bitmap);
        pbImgLoad.setVisibility(View.GONE);//把pbImgLoad隐藏起来（去掉）
        tvImgLoad.setVisibility(View.GONE);
    }
}
