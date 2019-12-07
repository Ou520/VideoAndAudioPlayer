package com.example.textthread.Thread;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.example.textthread.MainActivity;
import com.example.textthread.StartupPage1Activity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

//在别的地方要用的时候格式：new Thread(new MyThread(MainActivity.this)).start();
//或者格式：new MyThread(MainActivity.this).start();

public class MyThread extends Thread implements Runnable  {
    Handler handler;
    View v;
    public MyThread(View view) {
        v=view;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void run() {
        int index=10;
        while (index>=1)
        {
            Message msg=new Message();
            msg.what=011;
            msg.arg1=index;
            msg.obj="2233";
            handler.sendMessage(msg);
            try {
                Thread.sleep(1000);//或者SystemClock.sleep(1000);都是休眠方法，SystemClock.sleep(1000)不用做异常处理

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index--;
            startActivityForResult(new Intent(v.getContext(), StartupPage1Activity.class), 1);
        }
    }

    private void startActivityForResult(Intent intent, int i) {
    }
}



