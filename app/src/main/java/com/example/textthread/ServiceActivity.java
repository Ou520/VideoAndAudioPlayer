package com.example.textthread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.Service.BroadcastOnReceive;
import com.example.textthread.Service.MyBroadcastService;
import com.example.textthread.Service.MyResultService;
import com.example.textthread.Service.MyService;
import com.example.textthread.Service.NotificationService;
import com.example.textthread.Service.OnReceiveResult;

public class ServiceActivity extends Activity {
    Button bntService,bntResultService,bntBroadcastService,bntNotificationService;
    TextView tvBR;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        bntService=findViewById(R.id.bntService);
        bntResultService=findViewById(R.id.bntResultService);
        bntBroadcastService=findViewById(R.id.bntBroadcastService);
        bntNotificationService=findViewById(R.id.bntNotificationService);
        tvBR=findViewById(R.id.tvBR);

        //启动服务
        bntService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent启动Service，并传参数
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                intent.putExtra("MyKey","我的第一个Service!!!");
                startService(intent);
            }
        });

        //启动接收器服务
        bntResultService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义ResultReceiver接收器对象，并给他赋值（通过匿名调用OnReceiveResult类）
                ResultReceiver receiver = new OnReceiveResult(new Handler(),ServiceActivity.this,tvBR);//new Handler()有别的用途
                Intent intent =new Intent(ServiceActivity.this, MyResultService.class);
                intent.putExtra("MyKey1","我的第一个ResultService!!!");
                intent.putExtra("MyReceiver",receiver);
                startService(intent);

            }
        });
        //启动广播服务
        bntBroadcastService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ServiceActivity.this,MyBroadcastService.class);
                intent.putExtra("myBroadcastKey","这是我的第一个BroadcastService！！");
                startService(intent);//启动Service
            }
        });
        //启动通知服务
        bntNotificationService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ServiceActivity.this, NotificationService.class);
                startService(intent);
            }
        });
    }

    @Override
    //注册广播
    //Activity生命周期里的,获得activity的焦点方法
    protected void onResume() {
        super.onResume();
        IntentFilter filter =new IntentFilter(MyBroadcastService.ACTION);//定义一个Intent过滤器
        registerReceiver(broadcastReceiver,filter);//通过标记来注册广播
    }

    @Override
    //注销广播
    //Activity生命周期里的activity失去了焦点法
    protected void onPause() {
        super.onPause();
       unregisterReceiver(broadcastReceiver);//注销广播
    }
    //定义广播接收器,接收广播的对象可以是发广播的Activity，也可以同一个应用的别的Activity，也可以是别的应用的别的Activity
    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent !=null)
            {
                int resultCode =intent.getIntExtra("resultCode",0);
                if (resultCode==0x110)
                {
                    String broadcast =intent.getStringExtra("myBroadcast");
                    Toast.makeText(context, broadcast, Toast.LENGTH_SHORT).show();
                    tvBR.setText(broadcast);
                }
            }
        }
    };

    //定义一个广播接收器，重写的广播类（接收广播）
    //BroadcastReceiver是广播接收器`(不需要再组件上展示时用)
//   private BroadcastReceiver broadcastReceiver =new BroadcastOnReceive();
}
