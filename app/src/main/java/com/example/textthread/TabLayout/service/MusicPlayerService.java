package com.example.textthread.TabLayout.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.textthread.IMusicPlayerService;
import com.example.textthread.R;
import com.example.textthread.TabLayout.activity.SystemAudioPlayer;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.CacheUtils;
import java.io.IOException;
import java.util.ArrayList;

/*
  使用时要在功能清单文件（AndroidManifest.xml）中注册相应的服务（ Service有自己的生命周期）
 */

//创建自定义音乐播放的服务(服务默认在主线程执行)
public class MusicPlayerService extends Service {



    public static final String OPENAUDIO = "com.Ouwenbin.mobileplayer_OPENAUDIO";//服务的动作（意图）
    private ArrayList<MediaItem> mediaItems;//存放歌曲数据的数组列表
    private int position;//点击列表item的位置
    private MediaItem mediaItem;//歌曲数据的模型
    private MediaPlayer mediaPlayer;//定义媒体播放器
    private NotificationManager manager;//定义状态栏通知的管理类，负责发通知、清楚通知等
    private Notification.Builder builder;//定义状态栏通知（Notification：是具体的状态栏通知对象，可以设置icon、文字、提示声音、振动等等参数）
    private RemoteViews remoteViews;
    /*-----------------------播放器的播放模式--------------------------------*/
    public static final int REPEAT_NORMAL =1;//播放模式的标记，顺序播放
    public static final int REPEAT_SINGLE =2;//播放模式的标记，单曲循环播放
    public static final int REPEAT_ALL =3;//播放模式的标记，列表循环播放
    private int playmode =REPEAT_NORMAL;//设置播放器的播放模式默认是顺序播放



    @Override
    public void onCreate() {
        super.onCreate();
        playmode = CacheUtils.getPlaymode(this,"playmode");//从缓存中获取播放模式
        //加载音乐列表
        getDataFromLocal();
    }


    //获取并加载本地音乐列表（在子线程里）
    private void getDataFromLocal() {
        mediaItems =new ArrayList<>();//初始化数组（用之前一定要初始化（new））
        new Thread(){
            @Override
            public void run() {
                super.run();
                ContentResolver resolver=getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Audio.Media.DISPLAY_NAME,//音频文件在sdcard的名字
                        MediaStore.Audio.Media.DURATION,//音频总长
                        MediaStore.Audio.Media.SIZE,//音频文件的大小
                        MediaStore.Audio.Media.DATA,//音频的决对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };

                Cursor cursor =resolver.query(uri,objs,null,null,null);//定义一个游标

                if (cursor !=null){
                    while (cursor.moveToNext()){//通过循环把数据写入到模型中，在把有数据的模型存进数组中

                        MediaItem mediaItem = new MediaItem();//初始化模型类

                        //一个音频的信息
                        String name = cursor.getString(0);//音频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//音频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//音频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//音频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                        //把一个音频的数据存储到数组列表中
                        mediaItems.add(mediaItem);//写在上面和写在下面是一样的
                    }

                    cursor.close();//释放游标
                }
            }
        }.start();

    }


    //服务的方法
    //客户可以通过IBinder接口（onBind）和service进行通信
    @Override
    public IBinder onBind(Intent intent) {
        return stub;//返回AIDL文件（一定要）
    }


    //把AIDL文件生成的类，在服务中绑定（然后在onBind方法中把绑定好的stub返回去）
    private IMusicPlayerService.Stub stub =new IMusicPlayerService.Stub() {
        MusicPlayerService service =MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);//和服务里的方法绑定
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }
    };


/*-----------------------------音乐播放器的常用方法----------------------------------------------------*/
    /*
        使用步骤：
        1.先在AIDL文件的IMusicPlayerService.aidl里写好抽象方法
        2.在IMusicPlayerService.Stub(){}里，把重写好的抽象方法和服务中对应的方法进行绑定
        3.在服务中对应的方法里写想要的功能
     */

    //根据位置打开对应的音频文件，并且播放音频的方法
    private void openAudio(int position){

        if (mediaItems !=null && mediaItems.size() >0){
            mediaItem =mediaItems.get(position);
            if (mediaPlayer !=null){
//                mediaPlayer.release();//结束播放
                mediaPlayer.reset();//释放MediaPlayer
            }
            try {
                mediaPlayer =new MediaPlayer();//初始化MediaPlayer
                //设置播放监听：播放出错，播放完成，准备好
                //准备好
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                //播放完成
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                //播放出错
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                //设置MediaPlayer的播放地址
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this, "还没用数据！", Toast.LENGTH_SHORT).show();
        }
    }

    //媒体播放器准备好的监听
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            notifyChange(OPENAUDIO);//发广播通知Activity获取数据
            start();//开始播放
        }
    }

    //根据动作（意图）发广播
    private void notifyChange(String action) {
        Intent intent =new Intent(action);
        sendBroadcast(intent);
    }

    //媒体播放器播放完成的监听
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();//播放下一音频
        }
    }

    //媒体播放器播放出错的监听
    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();//播放下一音频
            return true;
        }
    }


//----------------------------------------播放音乐的方法------------------------------------------------------------------------

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    //开始播放音乐的方法
    private void start(){
        mediaPlayer.start();//开始播放

/*------------------------------------------------设置状态栏通知-----------------------------------------------------------------------*/
        //初始化状态栏通知的管理类，NotificationManager是一个系统Service，必须通过getSystemService()方法来获取。
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //当播放歌曲的时候，在状态显示正在播放，点击的时候，可以进入音乐播放页面
        Intent intent = new Intent(this, SystemAudioPlayer.class);
        intent.putExtra("Notification",true);//标识来自状态拦
       //初始化状态栏通知对象（Notification：是具体的状态栏通知对象）
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //定义RemoteViews对象，创建视图
       remoteViews =new RemoteViews("com.example.textthread",R.layout.music_player_notification_item);//为视图绑定自定义的布局文件
        //设置自定义布局里的控件的属性，先绑定控件，再设置控件的属性
        remoteViews.setTextViewText(R.id.tv_MusicPlay_Notification_Name,getName());
        remoteViews.setTextViewText(R.id.tv_MusicPlay_Notification_Artist,getArtist());

        //为remoteViews里的按钮设置点击事件（没完成）
//        Intent intent1 = new Intent("com.Ouwenbin.www.com");
//        PendingIntent contentIntents = PendingIntent.getBroadcast(this,0,intent1,0);
//        remoteViews.setOnClickPendingIntent(R.id.bnt_MusicPlayer,contentIntents);

         builder =new Notification.Builder(this);
        builder  .setSmallIcon(R.mipmap.ic_launcher_round)//设置通知显示的小图标(一定要设置)
                .setCustomContentView(remoteViews)//自定义通知的样式
                .setContentIntent(pendingIntent)//设置点击通知后跳转到别的Activity
                .setAutoCancel(false);//设置点击通知后，通知是否消失
        manager.notify(1, builder.build());

    }

    //暂停播放音乐的方法
    private void pause(){
        mediaPlayer.pause();
    }

    //停止播放音乐的方法
    private void stop(){
        mediaPlayer.stop();
    }

    //得到当前音频的播放进度的方法
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    //得到音频的总时长的方法
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    //得到音频的艺术家（作者）的方法
    private String getArtist(){
        return mediaItem.getArtist();
    }

    //得到歌曲的名字的方法
    private String getName(){
        return mediaItem.getName();
    }

    //得到歌曲的路径的方法
    private String getAudioPath(){
        return mediaItem.getData();
    }

    //播放上一个音频
    private void pre(){

        //1.根据当前的播放模式，设置上一个的位置
        setPrePosition();
        //2.根据当前的播放模式和下标位置去播放音频
        openPreAudio();
    }

    //设置上一首歌的位置
    private void setPrePosition() {
        int playmode = getPlayMode();
        if(playmode==MusicPlayerService.REPEAT_NORMAL){
            position--;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position--;
            if(position < 0){
                position = mediaItems.size()-1;
            }
        }else if(playmode ==MusicPlayerService.REPEAT_ALL){
            position--;
            if(position < 0){
                position = mediaItems.size()-1;
            }
        }else{
            position--;
        }
    }

    //根据设置好的位置去打开歌曲，并开始播放歌曲
    private void openPreAudio() {
        int playmode = getPlayMode();
        if(playmode==MusicPlayerService.REPEAT_NORMAL){
            if(position >= 0){
                //正常范围
                openAudio(position);
            }else{
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode ==MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if(position >= 0){
                //正常范围
                openAudio(position);
            }else{
                position = 0;
            }
        }
    }





    //播放下一音频的方法
    private void next(){
        //1.根据当前的播放模式，设置下一个的位置
        setNextPosition();
        //2.根据当前的播放模式和下标位置去播放音频
        openNextAudio();
    }

    ////设置下一首歌的位置
    private void setNextPosition() {
        int playmode = getPlayMode();
        if(playmode==MusicPlayerService.REPEAT_NORMAL){
            position++;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position++;
            if(position >=mediaItems.size()){
                position = 0;
            }
        }else if(playmode ==MusicPlayerService.REPEAT_ALL){
            position++;
            if(position >=mediaItems.size()){
                position = 0;
            }
        }else{
            position++;
        }
    }

   //根据设置好的位置去打开歌曲，并开始播放歌曲
    private void openNextAudio() {
        int playmode = getPlayMode();
        if(playmode==MusicPlayerService.REPEAT_NORMAL){
            if(position < mediaItems.size()){
                //正常范围
                openAudio(position);
            }else{
                position = mediaItems.size()-1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode ==MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if(position < mediaItems.size()){
                //正常范围
                openAudio(position);
            }else{
                position = mediaItems.size()-1;
            }
        }
    }


    //设置播放模式
    private void setPlayMode(int playmode){
        this.playmode=playmode;
        //把播放模式存入缓存中
        CacheUtils.putPlaymode(this,"playmode",playmode);

        //单曲循环
        if(playmode==MusicPlayerService.REPEAT_SINGLE){
            //单曲循环播放-不会触发播放完成的回调
            mediaPlayer.setLooping(true);//设置音频（视频）重复播放
        }else{
            //不循环播放
            mediaPlayer.setLooping(false);//取消音频（视频）重复播放
        }
    }

    //得到播放模式
    private int getPlayMode(){
        return playmode;
    }

    //是否在播放音频
    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }


}
