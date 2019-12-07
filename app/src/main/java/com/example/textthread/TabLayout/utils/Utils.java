package com.example.textthread.TabLayout.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;

import java.util.Formatter;
import java.util.Locale;

public class Utils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public Utils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     *
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    /**
     * 判断是否是网络的资源
     * @param uri
     * @return
     */
    public boolean isNetUri(String uri) {
        boolean reault = false;
        if (uri != null) {
            if (uri.toLowerCase().startsWith("http") || uri.toLowerCase().startsWith("rtsp") || uri.toLowerCase().startsWith("mms") ||uri.toLowerCase().startsWith("https")) {
                reault = true;
            }
        }
        return reault;
    }


    /**
     * 得到网络速度
     * 每隔两秒调用一次
     * @param context
     * @return
     */
    public String getNetSpeed(Context context) {
        String netSpeed = "0 kb/s";
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        netSpeed  = String.valueOf(speed) + " kb/s";
        return  netSpeed;
    }

    /**
     * 获取当前网络类型
     **/
    public String getNetworkType(Context context) {
        //结果返回值
        String netType = "无网络";
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = "正在使用：WIFI";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                //4G 网络
                netType = "正在使用：4G，请注意流量！";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                //3G网络   联通的3G为UMTS或HSDPA 电信的3G为EVDO
                netType = "正在使用：3G，请注意流量！";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                // 2G网络 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                netType = "正在使用：2G，请注意流量！";
            } else
                netType = "无网络";
        }
        return netType;
    }

}
