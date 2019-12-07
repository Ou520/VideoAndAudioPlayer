package com.example.textthread.TabLayout.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.textthread.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LaunchActivity extends Activity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.bntJump)
    Button bntJump;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    private int index=10;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        setData();
    }

    @SuppressLint("HandlerLeak")
    private void setData() {
        //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
        handler=new Handler(){
            @SuppressLint("NewApi")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==010)//通过msg.what判断消息是来自那个字线程的，并在UI线程的组件显示或者其他操作
                {
                    int index=msg.arg1;//获取消息携带的属性值
                    tvNumber.setText(String.valueOf(index));
                    if (index==1)
                    {
                        startActivity(new Intent(LaunchActivity.this, TabLayoutActivity.class));
                        finish();
                    }
                }

            }
        };

        //创建子线程，实现子线程（Thread）里的Runnable接口并实现接口里的run()方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (index>=1)
                {
                    Message msg=new Message();//创建Message对象
                    //给对象指定参数
                    msg.what=010;//标志（整型变量），用来判断消息是来自那个字线程
                    msg.arg1=index;//拿来传整型（int）数据和msg.arg2一样
                    msg.obj="2233";//参数是Object类型，拿来传其他类型的数据（String，模型，long等等）
                    handler.sendMessage(msg);//发送信息
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index--;
                }
            }
        }).start();
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.bntJump)
    public void onClick() {
        startActivity(new Intent(LaunchActivity.this, TabLayoutActivity.class));
        index=0;
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 强制竖屏设置
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
