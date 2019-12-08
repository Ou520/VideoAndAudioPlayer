package com.example.textthread.TabLayout.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.textthread.R;

import static android.app.Notification.VISIBILITY_SECRET;

public class NotificationUtils extends ContextWrapper {
    /*通知管理对象*/
    private NotificationManager notificationManager;

    /**
     * channel的ID
     */
    private static final String id = "channel_1";

    /**
     * channel的名称
     */
    public static final String name = "channel_name_1";

    /**
     * 通知生成类的构造方法
     */
    public NotificationUtils(Context context) {
        super(context);
    }

    /**
     * 创建NotificationChannel
     */
    @SuppressLint("NewApi")
    public void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.canBypassDnd();//可否绕过请勿打扰模式
        notificationChannel.canShowBadge();//桌面lanchener显示角标
        notificationChannel.enableLights(true);//闪光
        notificationChannel.shouldShowLights();//闪光
        notificationChannel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        notificationChannel.enableVibration(true);//是否允许震动
        notificationChannel.setVibrationPattern(new long[]{100, 100, 200});//设置震动方式（事件长短）
        notificationChannel.getAudioAttributes();//获取系统响铃配置
        notificationChannel.getGroup();//获取消息渠道组
        notificationChannel.setBypassDnd(true);
        notificationChannel.setDescription("description");
        notificationChannel.setLightColor(Color.GREEN);//制定闪灯是灯光颜色
        notificationChannel.setShowBadge(true);
        getNotificationManager().createNotificationChannel(notificationChannel);

    }

    /**
     * 获取通知管理者对象
     *
     * @return
     */
    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /**
     * 对应Android8.0生成notification的方法，通过此方法获取notification
     *
     * @param iconRes
     * @param title
     * @param content
     * @param pendingIntent
     * @return
     */
    @SuppressLint("NewApi")
    public Notification.Builder getChannelNotification(int iconRes, String title, String content, PendingIntent pendingIntent) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), id);
        builder.setSmallIcon(iconRes);
        builder.setAutoCancel(true);
        builder.setChannelId(id);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setNumber(3);
        builder.setContentIntent(pendingIntent);

        return builder;

    }

    /**
     * 对应Android8.0以下的notification对象
     * @param iconRes
     * @param title
     * @param content
     * @param pendingIntent
     * @return
     */
    public NotificationCompat.Builder getNotificationAPI25(int iconRes, String title, String content, PendingIntent pendingIntent){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(iconRes);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);

        return builder;
    }

    /**
     * 模拟一个显示进度条的通知
     * @param iconRes
     * @param title
     * @param content
     * @param pendingIntent
     */
    public   void sendProgressNotification(int iconRes,String title,String content,PendingIntent pendingIntent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
            final Notification.Builder builder=getChannelNotification(R.mipmap.ic_launcher,"這是通知頭","這是內容",pendingIntent);
            builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);//仅响一次，不会一直叮咚响
            final RemoteViews contentView=new RemoteViews(getPackageName(), R.layout.layout_remote);
            contentView.setTextViewText(R.id.remote_title,"题目");
            contentView.setTextViewText(R.id.remote_content,"内容");
            contentView.setOnClickPendingIntent(R.id.remote_icon,pendingIntent);
            builder.setCustomContentView(contentView);
            notificationManager.notify(1,builder.build());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<101;i++) {
                        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
                        builder.setProgress(100, i, false);
                        contentView.setProgressBar(R.id.remote_progress,100,i,false);
                        contentView.setTextViewText(R.id.remote_number,i+"%");
                        builder.setCustomContentView(contentView);
                        notificationManager.notify(1,builder.build());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }else {
            NotificationCompat.Builder builder=getNotificationAPI25(R.mipmap.ic_launcher,"這是通知頭","這是內容",pendingIntent);
            builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);//仅响一次，不会一直叮咚响
            notificationManager.notify(1,builder.build());
        }
    }


}