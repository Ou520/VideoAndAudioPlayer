package com.example.textthread.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

//广播类
public class MyBroadcastService extends IntentService {
    public static final String ACTION="com.example.textthread";//定义一个静态私有的标记(可以放在外部类)
    public MyBroadcastService() {
        super("broadcast");
    }

    @Override
    //启动Service时，自动执行这个方法，如果有参数，可以通过Intent获取
    protected void onHandleIntent( Intent intent) {
        if (intent !=null)
        {
            //获取ServiceActivity传过来的数据
            String myStr=intent.getStringExtra("myBroadcastKey");
            Log.v("myStr",myStr);
//            SystemClock.sleep(2000);
            /*
                执行下载网络的东西（耗时间的操作）
             */


            /*------------------------发送广播的思路----------------------
            广播 是 用”意图（Intent）“标识
            定义广播的本质 = 定义广播所具备的“意图（Intent）”
            广播发送 = 广播发送者 将此广播的“意图（Intent）”通过sendBroadcast（）方法发送出去
             */
            //发送带有标记的广播
            Intent myIntent =new Intent(ACTION);//通过标记来发送广播
            myIntent.putExtra("resultCode",0x110);//发送一个结果码回去
            myIntent.putExtra("myBroadcast","这是从MyBroadcastService发出的广播"+"\n"+myStr);
           sendBroadcast(myIntent);//携带myIntent里的数据发送广播
        }

    }
}
