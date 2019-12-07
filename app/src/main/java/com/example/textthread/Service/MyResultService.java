package com.example.textthread.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;

public class MyResultService extends IntentService {
    public MyResultService() {
        super("");
    }

    @Override
    //启动Service时，自动执行这个方法，如果有参数，可以通过Intent获取
    protected void onHandleIntent(Intent intent) {
        if (intent !=null)
        {
            String strValue=intent.getStringExtra("MyKey1");
            ResultReceiver receiver =(ResultReceiver) intent.getExtras().get("MyReceiver");
            Log.v("#",strValue);
            Bundle bundle =new Bundle();//定义一个Bundle对象
            bundle.putString("MyServiceKey","这是ResultReceiver返回的消息");//给Bundle对象赋值
            receiver.send(0x120,bundle);//发送消息，并携带bundle数据

        }
    }
}
