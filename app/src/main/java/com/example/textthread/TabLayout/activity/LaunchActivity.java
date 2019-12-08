package com.example.textthread.TabLayout.activity;

import android.Manifest;
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
import android.widget.Toast;

import com.example.textthread.R;
import com.example.textthread.permission.PermissionUtils;
import com.example.textthread.permission.request.IRequestPermissions;
import com.example.textthread.permission.request.RequestPermissions;
import com.example.textthread.permission.requestresult.IRequestPermissionsResult;
import com.example.textthread.permission.requestresult.RequestPermissionsResultSetApp;

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

    //调用自己写的IRequestPermissions接口，通过接口调用相应的方法
    private IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    private IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        setData();
    }

    @SuppressLint("HandlerLeak")
    private void setData() {

        /**
         * 强制竖屏设置
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //动态获取系统权限
        if (!requestPermissions())//判断系统是否已经执行requestPermissions()，如果没有就让他去执行，如果执行了就开始执行别的操作
        {
            return;
        }
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

    /*-------------动态获取安卓系统的权限，第一步在AndroidManifest.xml里申请相应的权限;第二步是在String[] permissions里给出你申请的权限---------------------*/
    //请求权限
    private boolean requestPermissions() {
        //需要请求的权限
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_CONTACTS
        };
        //开始请求权限
        return requestPermissions.requestPermissions(
                this,
                permissions,
                PermissionUtils.ResultCode1
        );
    }
    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)) {
            //输出授权结果
            Toast.makeText(this, "授权成功，请开始使用吧！", Toast.LENGTH_LONG).show();
            //请求的权限全部授权成功，此处可以做自己想做的事了
//            //加载首页面

        } else {
            //输出授权结果
            Toast.makeText(this, "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();

        }
    }

}
