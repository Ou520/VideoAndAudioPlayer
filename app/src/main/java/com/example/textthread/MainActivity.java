package com.example.textthread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.Thread.MyThread;
import com.example.textthread.permission.PermissionUtils;
import com.example.textthread.permission.request.IRequestPermissions;
import com.example.textthread.permission.request.RequestPermissions;
import com.example.textthread.permission.requestresult.IRequestPermissionsResult;
import com.example.textthread.permission.requestresult.RequestPermissionsResultSetApp;

public class MainActivity extends AppCompatActivity {
    Button bntJump;
    TextView tvNumber,tvAdvertising;
    Handler handler;
//----------------------------------------------------------------------------------------------------------------------------
    //调用自己写的IRequestPermissions接口，通过接口调用相应的方法
    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
//----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bntJump=findViewById(R.id.bntJump);
        tvNumber=findViewById(R.id.tvNumber);
        tvAdvertising=findViewById(R.id.tvAdvertising);
        //动态获取系统权限
        if (!requestPermissions())//判断系统是否已经执行requestPermissions()，如果没有就让他去执行，如果执行了就开始执行别的操作
        {
            return;
        }

//直接有TextView里的postDelayed：延迟显示来实现类似于子线程休眠的效果，这样就不用去创建子线程怎么麻烦了（实现类似动画播放的效果）
//------------去了解tvAdvertising.??里面的方法（重点）--------------------
        tvAdvertising.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAdvertising.setText("快打钱，到我的支付宝账号！");
                tvAdvertising.setTextSize(40);
                tvAdvertising.setTextColor(Color.BLUE);
            }
        }, 2500);//delayMills:延迟多少时间（单位毫秒）
        tvAdvertising.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAdvertising.setText("再不打钱过来，我们工商局见！");
                tvAdvertising.setTextSize(40);
                tvAdvertising.setTextColor(Color.RED);
            }
        }, 6000);//delayMills:延迟多少时间（单位毫秒）
//----------------------------------------------------------------------------------------------------
        /*为什么直接在子线程里绑定组件会报异常：因为
        只有UI线程才允许去控制组件，子线程访问网络，获取回来的数据展示到组件，子线程是不允许直接控制组件的
        只能将数据交给UI线程，由UI线程展示到组件上，子线程需要与UI线程进行通讯（子线程给UI线程发送消息，
        UI线程接收子线程发来的消息，然后将信息展示到相应的组件上；子线程与UI线程是借助Handler进行通讯的）
         */

   //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==010)//通过msg.what判断消息是来自那个字线程的，并在UI线程的组件显示或者其他操作
                {
                    int index=msg.arg1;//获取消息携带的属性值
//                    String myMessage=msg.obj.toString();//获取msg.ob里的消息携带的属性值，要做相应的类型转换
//                    Toast.makeText(MainActivity.this,myMessage , Toast.LENGTH_SHORT).show();
                    tvNumber.setText(String.valueOf(index));
                }
            }
        };

      //创建子线程，实现子线程（Thread）里的Runnable接口并实现接口里的run()方法
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       int index=10;
                       while (index>=1)
                        {
                            Message msg=new Message();//创建Message对象
                            //给对象指定参数
                            msg.what=010;//标志（整型变量），用来判断消息是来自那个字线程
                            msg.arg1=index;//拿来传整型（int）数据和msg.arg2一样
                            msg.obj="2233";//参数是Object类型，拿来传其他类型的数据（String，模型，long等等）
                            handler.sendMessage(msg);//发送信息
                            /*//handler的其他方法
                            sendEmptyMessage(int what):发送空消息
                            sendEmptyMessageDelayed(int what,long delayMillis):指定延时多少毫秒后发送空信息
                            sendMessage(Message msg):立即发送信息
                            sendMessageDelayed(Message msg):指定延时多少毫秒后发送信息
                             */
                            try {
                               Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                                index--;
                        }
                       startActivityForResult(new Intent(MainActivity.this, StartupPage1Activity.class), 1);
                       finish();
                    }
                }).start();

//        创建子线程
//        MyThread myThread=new MyThread(this);
//        myThread.start();

        //给bntJump按钮写监听，实现页面跳转
        bntJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, StartupPage1Activity.class), 1);
                finish();
                onDestroy();
            }
        });
    }

/*-------------动态获取安卓系统的权限，第一步在AndroidManifest.xml里申请相应的权限;第二步是在String[] permissions里给出你申请的权限---------------------*/
    //请求权限
    private boolean requestPermissions(){
        //需要请求的权限
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
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
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if(requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
            //请求的权限全部授权成功，此处可以做自己想做的事了


            //输出授权结果
            Toast.makeText(MainActivity.this,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
        }else{
            //输出授权结果
            Toast.makeText(MainActivity.this,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
        }
    }

}
