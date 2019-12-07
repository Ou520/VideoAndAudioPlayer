package com.example.textthread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.ImdAsynTaak.ImgAsyncTask;
import com.example.textthread.ImdAsynTaak.NetworkUtil;
import com.example.textthread.ImdAsynTaak.SavaImage;
import com.example.textthread.ImdAsynTaak.Utils.HttpBitmapUtils;

public class ImgShow extends Activity {
    ProgressBar pbImgLoad;
    ImageView imgShow;
    TextView tvImgLoad;
    Button bntSavaImage;
    String url;
    Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_show);
        pbImgLoad=findViewById(R.id.pbImgLoad);
        imgShow=findViewById(R.id.imgShow);
        tvImgLoad=findViewById(R.id.tvImgLoad);
        bntSavaImage =findViewById(R.id.bntSavaImage);

        //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==010)//通过msg.what判断消息是来自那个字线程的，并在UI线程的组件显示或者其他操作
                {
                    Bitmap bitmap = (Bitmap) msg.obj;//创建位图对象并赋值Message传来的值
//                    imgShow.setImageBitmap(bitmap);//在ImageView显示图片（拿来判断bitmap里有没有东西）
                    if (bitmap!=null)
                    {
                        new SavaImage(ImgShow.this, (int) System.currentTimeMillis()).getSavaImge(bitmap, Environment.getExternalStorageDirectory()+"/Pictures/");//通过匿名调用SavaImage类里面的getSavaImge方法（其中参数：bitmap为位图；path为文件的保存路径）
                    }else
                        {
                            Toast.makeText(ImgShow.this, "bitmap为空！", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        };

        if (NetworkUtil.isReady(getApplicationContext()))//判断网络是否可用
                {
                    //接收ImgAsyncTaskActivity传过来的网址
                     Intent intent=getIntent();
                      url = intent.getStringExtra("urlKey");//图片地址
                     new ImgAsyncTask(imgShow,pbImgLoad,tvImgLoad).execute(url);//通过外部类获取网络数据并显示出

                }else
                {
                    //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
                    new AlertDialog.Builder(ImgShow.this)
                            .setTitle("当前网络不可用")
                            .setMessage("请设置网络！！！")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", null)
                            .show();
                }


                //ImageView设置监听
                imgShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivityForResult(new Intent(ImgShow.this, JsonAsyyncTaskActivity.class), 111);
//                        finish();
                    }
                });
        //写监听保存图片
        bntSavaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      Bitmap  bitmap=new HttpBitmapUtils().getBitmap(url);//定义一个位图对象并赋值
                        Message msg=new Message();//创建Message对象
                        //给对象指定参数
                        msg.what=010;//标志（整型变量），用来判断消息是来自那个字线程
                        msg.obj=bitmap;//给msg赋值一个
                        handler.sendMessage(msg);//发送信息
                    }
                }).start();//启动线程
            }
        });
    }
    public void ReturnOnClick(View v)
    {
//        startActivityForResult(new Intent(ImgShow.this, ImgAsyncTaskActivity.class), 1);
        finish();
    }
}
