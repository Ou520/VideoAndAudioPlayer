package com.example.textthread.ImdAsynTaak;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {
    public static boolean isReady(Context context)
    {
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*
        //获取当前网络的状态
         NetworkInfo[] infos =cm.getAllNetworkInfo();
         for (NetworkInfo i : infos)//打印当前的网络状态信息
         {
             Log.v(i.getTypeName(),i.isAvailable() + "#"+i.isConnected());
         }
         */
        //检查网络是否可以
        NetworkInfo info =cm.getActiveNetworkInfo();
        if (info!=null && info.isConnected())//判断顺序不能变
        {
            return true;
        }
         return false;
    }
}
