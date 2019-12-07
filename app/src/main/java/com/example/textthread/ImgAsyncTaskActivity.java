package com.example.textthread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.HttpUtils.HttpUtils;
import com.example.textthread.ImdAsynTaak.ImgAsyncTask;
import com.example.textthread.ImdAsynTaak.NetworkUtil;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImgAsyncTaskActivity extends Activity {
    Button bntImgLoad;
    ProgressBar pbImgLoad;
    ImageView img;
    TextView tvImgLoad;
    EditText etImg;
    String url;//定义字符串变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_async_task);
        bntImgLoad=findViewById(R.id.bntImgLoad);
        pbImgLoad=findViewById(R.id.pbImgLoad);
        img=findViewById(R.id.img);
        tvImgLoad=findViewById(R.id.tvImgLoad);
//        etImg=findViewById(R.id.etImg);

        bntImgLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                //获取网络状态，并在日志中打印
                NetworkUtil.isReady(getApplicationContext());//getApplicationContext()或ImgAsyncTaskActivity.this者是当前的上下文，把当前上下文传到NetworkUtil里
                */
                if (NetworkUtil.isReady(getApplicationContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
                {
                    //图片地址
                     url = "http://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/f31fbe096b63f624df79877a8f44ebf81a4ca3a7.jpg";
//                     url=etImg.getText().toString();//给url赋值
                    //方法一,通过异步任务处理网上的图片
                     new ImgAsyncTask(img,pbImgLoad,tvImgLoad).execute(url);//通过外部类获取网络数据并显示出

                    //方法二，通过第三方位图处理框架Picasso来处理网上的图片
//                    Picasso.with(ImgAsyncTaskActivity.this).load(url).into(img);

                }else
                    {
                        //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
                        new AlertDialog.Builder(ImgAsyncTaskActivity.this)
                                .setTitle("当前网络不可用")
                                .setMessage("  请设置网络！！！")
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


            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //让intent携带数据跳转到ImgShow
                Intent intent =new Intent(ImgAsyncTaskActivity.this, ImgShow.class);
                intent.putExtra("urlKey",url);
                startActivity(intent);
                finish();
            }
        });
    }

}
