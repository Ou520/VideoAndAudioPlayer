package com.example.textthread.Service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.textthread.ImdAsynTaak.SavaImage;
import com.example.textthread.ImgShow;
import com.example.textthread.NotificationImagerShowActivity;
import com.example.textthread.R;
import com.example.textthread.StartupPage1Activity;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import static android.content.ContentValues.TAG;

public class NotificationService extends IntentService {
    int fileName = (int) System.currentTimeMillis();
    public NotificationService() {
        super("Notification");
    }
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        String url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559580721078&di=db06e34d63af7dd47f42f9ebeb7d5ba0&imgtype=0&src=http%3A%2F%2Fimg.kang5.net%2F87%2F7a%2F877ad596bc92e060.jpg";

//--------------------------------------------------------------自定义布局的通知------------------------------------------------------------------------------------------------
        //定义NotificationManager对象；//发送通知
        final NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);// getSystemService(NOTIFICATION_SERVICE)是获取系统的的通知的Service服务
        //创建Notification.Builder对象，设置通知的样式
        final Notification.Builder builder =new Notification.Builder(this);
        //定义PendingIntent里的参数，并定义PendingIntent和赋值
        final int requestCode =(int)( System.currentTimeMillis() +(1+Math.random()*(10-1+1) + new Random().nextInt()));//定义一个整型的请求码(java产生随机数的三种方法)

        Intent myIntent =new Intent(this, NotificationImagerShowActivity.class);//定义Intent对象跳转Activity，可以携带参数
        myIntent.putExtra("fileNameKey",fileName);
//        myIntent.putExtra("Key",data);//通过Intent把数据传到别的Activity

        int flags = PendingIntent.FLAG_CANCEL_CURRENT;//设置第二条通知显示的方式是替换第一条
        PendingIntent pendingIntent =PendingIntent.getActivity(this,requestCode,myIntent,flags);
        //获取系统当前的时间，并对系统时间进行格式处理
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(/*"yyyy年*/"M月dd日 HH:mm"/* HH:mm:mm */);//日期的格式处理
        Date date=new Date(Calendar.getInstance().getTimeInMillis());//System.currentTimeMillis():获取系统当前的数据（long类型的数据）

        //定义RemoteViews对象，创建视图
        RemoteViews remoteViews =new RemoteViews("com.example.textthread",R.layout.notification_item);//为视图绑定自定义的布局文件
        remoteViews.setTextViewText(R.id.tvTime,String.valueOf(simpleDateFormat.format(date)));//设置自定义布局里的控件的属性，先绑定控件，再设置控件的属性

        //        通知的小图标一定要给否则会报错
        builder
//                .setContentTitle("Title")//设置通知的标题
//                .setContentText("myText"+requestCode)//设置通知的内容
                .setSmallIcon(R.mipmap.ic_launcher_round)//设置通知显示的小图标
//                .setLargeIcon()//设置通知显示的大图标
                .setCustomContentView(remoteViews)//自定义通知的样式
                .setContentIntent(pendingIntent)//设置点击通知后跳转到别的Activity
//                .setWhen(System.currentTimeMillis())//设置通知显示的时间
//                .setSound()//设置接收通知时的声音
//                .setLights()//设置提示灯的样式
//                .setProgress(0,0,true)//设置通知显示下载进度(没有进度变化)
                .setAutoCancel(true);//设置点击通知后，通知是否消失

        // 下载网络图片，下载时在通知里显示进度变化，下载完成时给出提示，点击通知后跳转别的Activity
        Bitmap bitmap=null;//定义位图对象
        OutputStream os = null;//定义输出流对象
        InputStream is =null;//定义输入流对象
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

                remoteViews.setProgressBar(R.id.pbNotification,100,progress,false);//设置自定义布局里的控件的属性，先绑定控件，再设置控件的属性
                remoteViews.setTextViewText(R.id.tvItemLoad,progress+"%");//进度变化


                manager.notify(0,builder.build());//发送通知；di可以看做的一个标记，用来区分不同的通知；如果两次通知的id相同后面那个通知可以对前一个通知进行更新或覆盖

            }
            os.flush();//刷新一下数据
            byte[] bitmapData = bos.toByteArray();//把字节输出流转换为字节数组赋值给bitmapData
            bitmap = BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length);//用位图的工厂类，对整个字节数组进行解码（看一下里面的其他类）//把输入流转换为位图，并赋值给bitmap
            //通过bitmap下载图片到本地
            if (bitmap!=null)
            {
                new SavaImage(this,fileName).getSavaImge(bitmap, Environment.getExternalStorageDirectory()+"/Pictures/");//通过匿名调用SavaImage类里面的getSavaImge方法（其中参数：bitmap为位图；path为文件的保存路径）
            }else
            {
                Log.v("#","bitmap为空！");
            }


            remoteViews.setTextViewText(R.id.tvTime,String.valueOf(simpleDateFormat.format(date)));//设置自定义布局里的控件的属性，先绑定控件，再设置控件的属性
            remoteViews.setTextViewText(R.id.tvNotification,"图片下载完成！");//设置自定义布局里的控件的属性，先绑定控件，再设置控件的属性


            manager.notify(0,builder.build());//发送通知；di可以看做的一个标记，用来区分不同的通知；如果两次通知的id相同后面那个通知可以对前一个通知进行更新或覆盖

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





        /*
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        for (incr = 0; incr <= 100; incr+=5) {
                            builder.setProgress(100, incr, false);
                             manager.notify(0,builder.build());//di可以看做的一个标记，用来区分不同的通知；如果两次通知的id相同后面那个通知可以对前一个通知进行更新或覆盖
                            try {
                                Thread.sleep(1*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        builder.setContentText("下载完成！")//下载完成
                                .setContentTitle("图片")
                                .setProgress(0,0,false);    //移除进度条
                         manager.notify(0,builder.build());//di可以看做的一个标记，用来区分不同的通知；如果两次通知的id相同后面那个通知可以对前一个通知进行更新或覆盖
                        Log.v("随机数", String.valueOf(requestCode));
                    }
                }
        ).start();

         */
    }
}
