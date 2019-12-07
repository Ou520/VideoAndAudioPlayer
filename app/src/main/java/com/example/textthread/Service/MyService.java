package com.example.textthread.Service;


import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

//当需要启动子线程处理一些事情（网络数据下载），相对比较耗时用Service；
// 如果耗时间短的话或者需要和Activity进行交换时，应选择AsyncTask
public class MyService extends IntentService {
    public MyService() {
        super("Text");//当前运行的Service的名字
    }

    @Override
    //启动Service时，自动执行这个方法，如果有参数，可以通过Intent获取
    protected void onHandleIntent(Intent intent) {
        String value=intent.getStringExtra("MyKey");
        if (value !=null && value !="")
        {
            Log.v("Value",value);
        }
        //获取系统当前运行的所有Service
       ActivityManager activityManager =(ActivityManager) getSystemService(ACTIVITY_SERVICE);//获取系统的的Service
        List<ActivityManager.RunningServiceInfo> list =activityManager.getRunningServices(40);
        for (ActivityManager.RunningServiceInfo info : list)//对list进行遍历输出
        {
            Log.v("系统当前运行的Service",info.service.getClassName());
        }
    }
}
