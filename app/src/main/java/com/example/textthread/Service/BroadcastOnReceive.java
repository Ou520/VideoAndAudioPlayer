package com.example.textthread.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//定义一个外部类并继承BroadcastReceiver是广播接收器
public class BroadcastOnReceive extends BroadcastReceiver {

    @Override
    //重写里面的onReceive
    public void onReceive(Context context, Intent intent) {
        if (intent !=null)
        {
            int resultCode =intent.getIntExtra("resultCode",0);
            if (resultCode==0x110)
            {
                String broadcast =intent.getStringExtra("myBroadcast");
                Toast.makeText(context, broadcast, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
