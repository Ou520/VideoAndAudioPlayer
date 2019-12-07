package com.example.textthread.TabLayout.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.textthread.R;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;


//系统播放器
public class VitamioVideoPlayer extends Activity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.tv_system_time)
    TextView tvSystemTime;
    @BindView(R.id.tv_buffer_netSpeed)
    TextView tv_buffer_netSpeed;
    @BindView(R.id.tv_loading_netSpeed)
    TextView tv_loading_netSpeed;
    @BindView(R.id.bnt_voice)
    Button bntVoice;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.seekbar_voice)
    SeekBar seekbarVoice;
    @BindView(R.id.seekbar_Light)
    SeekBar seekbar_Light;
    @BindView(R.id.seekbar_video)
    SeekBar seekbarVideo;
    @BindView(R.id.bnt_exit)
    Button bntExit;
    @BindView(R.id.bnt_video_pre)
    Button bntVideoPre;
    @BindView(R.id.bnt_video_start_pause)
    Button bntVideoStartPause;
    @BindView(R.id.bnt_video_next)
    Button bntVideoNext;
    @BindView(R.id.bnt_video_siwch_screen)
    Button bntVideoSiwchScreen;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.LL_Volume)
    LinearLayout LL_Volume;
    @BindView(R.id.LL_Brightness)
    LinearLayout LL_Brightness;
    @BindView(R.id.ll_buffer)
    LinearLayout ll_buffer;
    @BindView(R.id.ll_loading)
    LinearLayout ll_loading;
    @BindView(R.id.media_controller)
    RelativeLayout media_controller;
    @BindView(R.id.videoview)
    VideoView videoview;

    private Uri uri;
    //视频进度的更新
    private static  final int PROGRESS =1;
    //视频的总时长
    private int duration;
    private Utils utils;
//    监听电量变化的广播
    private MyReceiver receiver;
    //传入进来的视频列表
    private ArrayList<MediaItem> mediaItems;
    //要播放的列表中的具体位置
    private int position;
    //1.定义手势识别器
    private GestureDetector detector;
    //是否显示控制面板
    private boolean isshowMediaController =false;
    //调节声音
    private AudioManager am;
    //当前的音量(0-15)
    private int currentVoice;
    //最大的音量
    private int maxVoice;
   //当前的亮度（0-255）
    private int screenBrightness;
    //设置是否静音
    private boolean isMute =false;
   //屏幕的宽
    private int screenWidth = 0;
    //屏幕的高
    private int screenHeight = 0;
    //是否是网络的URI
    private boolean isNetUri;
    //视频卡顿的监听方式(还没用)
    private boolean isUseSysten =false;
    //上一次的视频播放进度
    private int precurrentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_vitamio_video_player);
        ButterKnife.bind(this);
        initData();
        //获取数据
        getData();
        //设置数据
        setData();
        //监听
        setListener();

    }

    private void initData() {
        //工具类的初始化
        utils =new Utils();
        //注册电量广播
        receiver=new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //当电量变化的时候发广播
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(receiver,intentFilter);

        //2.实例化手势识别器，并且重写双击，单击，长按
        detector =new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            /*
            //长按
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(SystemVideoPlayer.this, "长按全屏显示", Toast.LENGTH_SHORT).show();
            }
             */

            //双击
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                StarAndPause();//播放和暂停
//                Toast.makeText(SystemVideoPlayer.this, "双击", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
            //单击
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayer.this, "单击", Toast.LENGTH_SHORT).show();
                //如果是显示就隐藏。如果隐藏就显示。（控制面板）
                if (isshowMediaController){
                    //隐藏
                    hideMediaController();
                    //把隐藏消息移除
                    handler.removeMessages(2);
                }else {
                    //显示
                    showMediaController();
                    //发消息通知隐藏控制面板
                    handler.sendEmptyMessageDelayed(2,3000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        //得到屏幕的宽和高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //1.得到音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice =am.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前的音量
        maxVoice =am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取最大音量
        //获取当前的亮度
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //开始更新网速
        handler.sendEmptyMessage(3);

    }


    private void getData() {
        //得到播放地址
        uri = getIntent().getData();//数据来自于外部应用
        mediaItems= (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position=getIntent().getIntExtra("position",0);
    }

    private void setData() {
        //为VideoView设置地址
        if (mediaItems !=null && mediaItems.size()>0){
            MediaItem mediaItem=mediaItems.get(position);
            tvName.setText(mediaItem.getName());//设置视频的名称
            isNetUri =utils.isNetUri(mediaItem.getData());//判断URI是否是网络连接
            videoview.setVideoPath(mediaItem.getData());
        }else if (uri !=null){
            tvName.setText(uri.toString());
            isNetUri =utils.isNetUri(uri.toString());
            videoview.setVideoURI(uri);
        }else {
            Toast.makeText(this, "地址为空,没有数据！", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
        //2.设置最大音量和seekBar做关联
        seekbarVoice.setMax(maxVoice);

        //设置亮度的SeekBar
        seekbar_Light.setMax(255);
        seekbar_Light.setProgress(screenBrightness);
        //设置当前的音量
        seekbarVoice.setProgress(currentVoice);
        //隐藏横竖屏切换
        bntVideoSiwchScreen.setVisibility(View.INVISIBLE);

    }

    private void setListener() {
        //准备好的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        //播放出错的监听
        videoview.setOnErrorListener(new MyOnErrorListener());

        //播放完成的监听
        videoview.setOnCompletionListener(new MyOnCompletionListener());

        //设置视频进度的SeekBar的状态监听
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        //设置音量大小的SeekBar的监听
        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
        //设置亮度大小的SeekBar的监听
        seekbar_Light.setOnSeekBarChangeListener(new LightOnSeekBarChangeListener());
        //监听视频播放的卡顿-系统的APi（17以上）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoview.setOnInfoListener(new MyOnInfoListener());
        }
    }



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2 ://3秒
                    hideMediaController();//隐藏控制面板
                    break;
                case PROGRESS :
                    //1.得到当前的播放进度
                    int currentPosition = (int) videoview.getCurrentPosition();
                    //2.SeekBar.setProgress(当前的进度);
                    seekbarVideo.setProgress(currentPosition);
                    //更新播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition)+" / "+utils.stringForTime(duration));
                   //设置系统时间
                    String time= getSystemTime();
                    tvSystemTime.setText(time);

                    //缓存进度的更新
                    if (isNetUri){
                        //只有网络资源才有缓存效果
                        int buffer = videoview.getBufferPercentage();//缓存是0-100的值
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int secondaryProgress =totalBuffer/100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);

                    }else {
                        //本地视频没有缓存效果
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //监听网络视频播放的卡顿(直播时不能用)
                    /*

                        if (!isUseSysten && videoview.isPlaying()){
                        int buffer = currentPosition - precurrentPosition;
                        if (buffer < 500){
                            //视频卡了
                            ll_buffer.setVisibility(View.VISIBLE);
                        }else {
                            //视频不看了
                            ll_buffer.setVisibility(View.GONE);
                        }
                        precurrentPosition=currentPosition;
                    }

                     */

                    //3.每秒更新一次
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
                case 3://2秒
                    //1.得到网速
                    String netSpeed =utils.getNetSpeed(VitamioVideoPlayer.this);
                    //显示网速
                    tv_loading_netSpeed.setText("玩命加载中..."+netSpeed);
                    tv_buffer_netSpeed.setText("缓存中..."+netSpeed);
                    //每两秒调用一次
                    handler.removeMessages(3);
                    handler.sendEmptyMessageDelayed(3,2000);
                    //隐藏布局
                    LL_Volume.setVisibility(View.GONE);
                    LL_Brightness.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    //获取当前系统时间
    private String getSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// yyyy年MM月dd日HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }



   //通过广播来获取系统电量的变化情况，并设置电池图标的变化
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level =intent.getIntExtra("level",0);//0-100
            setBattery(level);//设置电池图片的变化
        }
    }

    //设置电池图标的状态
    private void setBattery(int level) {
        if (level<=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if (level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if (level<=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if (level<=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if (level<=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if (level<=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }



    ////设置视频的控制面板的按钮的点击事件
    @OnClick({R.id.bnt_voice, R.id.bnt_exit, R.id.bnt_video_pre, R.id.bnt_video_start_pause, R.id.bnt_video_next, R.id.bnt_video_siwch_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnt_voice:
                isMute = !isMute;
                updataVoice(currentVoice,isMute);//设置静音
                break;
            case R.id.bnt_exit:
                finish();
                break;
            case R.id.bnt_video_pre:
                playPreVideo();
                break;
            case R.id.bnt_video_start_pause:
                StarAndPause();
                break;
            case R.id.bnt_video_next:
                playNextVideo();
                break;
            case R.id.bnt_video_siwch_screen:
                //判断当前的屏幕状态是横屏还是竖屏
                Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
                int orientation = mConfiguration.orientation; //获取屏幕方向
                if (orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
                    //横屏时
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                    bntVideoSiwchScreen.setBackgroundResource(R.drawable.bnt_video_siwch_screen_full_selector);
//                    ll_Content.setVisibility(View.VISIBLE);
                } else if (orientation == mConfiguration.ORIENTATION_PORTRAIT) {
                    //竖屏时
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                    bntVideoSiwchScreen.setBackgroundResource(R.drawable.bnt_video_siwch_screen_default_selector);
//                    ll_Content.setVisibility(View.GONE);
                }

                break;
        }
        handler.removeMessages(2);
        handler.sendEmptyMessageDelayed(2,3000);
    }

    //播放和暂停
    private void StarAndPause() {
        if (videoview.isPlaying()){
            //视频在播放-设置为暂停
            videoview.pause();
            //按钮状态设置为播放
            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_video_start_selector);
        }else {
            //视频的播放
            videoview.start();
            //按钮的状态设置为暂停
            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
        }
    }

    //播放上一个视频
    private void playPreVideo() {
        if (mediaItems !=null && mediaItems.size()>0){
            //播放上一个
            position--;
            if (position >=0){
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri =utils.isNetUri(mediaItem.getData());
                videoview.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
                bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                ll_loading.setVisibility(View.VISIBLE);
            }
        }else {
            setButtonState();
        }

    }

    //播放下一个视频
    private void playNextVideo() {
        if (mediaItems !=null && mediaItems.size()>0){
            //播放下一个
            position++;
            if (position < mediaItems.size()){
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri =utils.isNetUri(mediaItem.getData());
                videoview.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
                bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                ll_loading.setVisibility(View.VISIBLE);
            }
        }else {
            setButtonState();
        }
    }

    //按钮的状态方法
    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                setEnable(false);
            } else if (mediaItems.size() == 2) {
                if (position == 0) {
                    bntVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    bntVideoPre.setEnabled(false);
                    bntVideoNext.setBackgroundResource(R.drawable.bnt_video_next_selector);
                    bntVideoNext.setEnabled(true);

                } else if (position == mediaItems.size() - 1) {
                    bntVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    bntVideoNext.setEnabled(false);

                    bntVideoPre.setBackgroundResource(R.drawable.bnt_video_pre_selector);
                    bntVideoPre.setEnabled(true);

                }
            } else {
                if (position == 0) {
                    bntVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    bntVideoPre.setEnabled(false);
                } else if (position == mediaItems.size() - 1) {
                    bntVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    bntVideoNext.setEnabled(false);
                } else {
                    setEnable(true);
                }
            }
        } else if (uri != null) {
            //两个按钮设置灰色
            setEnable(false);
        }
    }

    //按钮的状态方法
    private void setEnable(boolean isEnable) {
        if (isEnable) {
            bntVideoPre.setBackgroundResource(R.drawable.bnt_video_pre_selector);
            bntVideoPre.setEnabled(true);
            bntVideoNext.setBackgroundResource(R.drawable.bnt_video_next_selector);
            bntVideoNext.setEnabled(true);
        } else {
            //两个按钮设置灰色
            bntVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            bntVideoPre.setEnabled(false);
            bntVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            bntVideoNext.setEnabled(false);
        }

    }

    //声音大小的SeekBar的拖动监听
    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                if (progress >0){
                    isMute =false;
                }else {
                    isMute =true;
                }
                updataVoice(progress,isMute);//更新音量变化
            }
        }

        //当手指触碰的时候回调这个方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(2);
        }

        //当手指离开的时候回调这个方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(2,3000);
        }
    }
    //更新音量变化（设置音量的大小）
    private void updataVoice(int progress,boolean isMute) {
        if (isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekbarVoice.setProgress(0);

        }else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            seekbarVoice.setProgress(progress);
            currentVoice =progress;
        }

    }

    //亮度大小的SeekBar的拖动监听
    class LightOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{
        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        //当手指触碰的时候回调这个方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        //当手指离开的时候回调这个方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int seekPos = seekBar.getProgress();
            setScreenBrightness(seekPos);
            handler.sendEmptyMessageDelayed(2,3000);
        }
    }

    //网络视频播放卡顿的监听(系统自带的方法)，也可以通过（当前播放进度 - 上一次播放进度 < 0 ,就卡)
    class MyOnInfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START ://视频卡顿，拖动卡
//                    Toast.makeText(SystemVideoPlayer.this, "视频正在加载中....!", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END ://视频卡顿结束，拖动卡结束
//                    Toast.makeText(SystemVideoPlayer.this, "视频加载完成!", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }


    //    设置当前窗口亮度
    private void setScreenBrightness(int value) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = value / 255.0f;
        window.setAttributes(lp);
    }

    //视频进度的SeekBar的拖动监听
    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser==true){
                videoview.seekTo(progress);
            }
        }

        //当手指触碰的时候回调这个方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(2);

        }

        //当手指离开的时候回调这个方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(2,3000);
        }
    }


    //准备播放时的监听（MediaPlayer）
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码准备好的时候
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoview.start();//开始播放
            //关联总长度
            duration= (int) videoview.getDuration();
            seekbarVideo.setMax(duration);
            //默认是隐藏控制面板
            hideMediaController();
            //2.发消息
            handler.sendEmptyMessage(PROGRESS);
            //把加载页面隐藏起来
            ll_loading.setVisibility(View.GONE);
        }
    }

    //显示控制面板
    private void showMediaController(){
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        isshowMediaController=true;
    }
    //隐藏控制面板
    private void hideMediaController(){
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        isshowMediaController=false;
    }


    //播放出错的监听
    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(VitamioVideoPlayer.this, "播放出错了....！", Toast.LENGTH_SHORT).show();
            showErrorDialog();//播放出错跳出对话框
            return true;
        }
    }
    //播放出错跳出对话框
    private void showErrorDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("抱歉无法播放视频！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    //播放完成的监听
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextVideo();
            Toast.makeText(VitamioVideoPlayer.this, "播放完成了,跳到下一个视频！", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------------------------------------------Activity的触摸事件--------------------------------------------------------------------------------
    //按下时的X，Y的值
    private float startY;
    private float startX;
    //屏幕的高
    private float tochRang;
    //一按下的音量
    private int mVol;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int FinalBrightness;//最终的亮度
        int FinalVolume;//最终的音量
        int FinalProgress;//最终的视频进度

        //3.把事件传递个手势识别器
        detector.onTouchEvent(event);

        //判断触摸屏幕的方式
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN ://手指按下
                //1.按下时记录值
                startY = event.getY();
                startX =event.getX();
                mVol =am.getStreamVolume(AudioManager.STREAM_MUSIC);
                tochRang =Math.min(screenHeight,screenWidth);
                handler.removeMessages(2);
                break;
            case MotionEvent.ACTION_MOVE ://手指移动
                //2.移动时记录值
                float endY =event.getY();//移动时Y的值（会变化）
                float endX = event.getX();//移动时X的值（会变化）
                float distanceY = startY-endY ;//Y移动的距离（上取正，下取负）
                float distanceX = endX-startX ;//X移动的距离（右取正，左取负）

                //触摸面积小于屏幕宽度的一半的位置（左边屏幕）
                if(endX < screenWidth/2 ) {
                    //向上滑动时的操作
                    /*
                        Math.abs(x)=|x|；如果参数是非负数，则返回该参数。如果参数是负数，则返回该参数的相反数。
                     */
                    if (distanceY > 0 && (Math.abs(distanceY) > 200) && endY>400) { //向上滑动（distanceY > 0），并且滑动的距离大于200，并且在距离屏幕大于400xp的地方滑动时执行下面的方法
                        Toast.makeText(this, "向上滑动", Toast.LENGTH_SHORT).show();
                        Log.v("向上滑动", String.valueOf(distanceY));
                        LL_Brightness.setVisibility(View.VISIBLE);
                        LL_Volume.setVisibility(View.GONE);
                        //改变亮度 = （滑动屏幕的距离： 总距离）*亮度最大值
                        float delta1 = ((distanceY / tochRang))/15 * 255;
                        //最终亮度 = 原来的 + 改变亮度；
                        FinalBrightness = (int) Math.min(Math.max(screenBrightness + delta1, 0), 255);
                        if (delta1 != 0) {
                            setScreenBrightness(FinalBrightness);
                        }
                        seekbar_Light.setProgress(FinalBrightness);
                        screenBrightness = FinalBrightness;
                    } else //向下滑动时的操作
                        if (distanceY < 0 && (Math.abs(distanceY) > 200) && endY>400) {
                            Toast.makeText(this, "向下滑动", Toast.LENGTH_SHORT).show();
                            Log.v("向下滑动", String.valueOf(distanceY));
                            LL_Brightness.setVisibility(View.VISIBLE);
                            LL_Volume.setVisibility(View.GONE);
                            //改变亮度 = （滑动屏幕的距离： 总距离）*亮度最大值
                            float delta1 = ((distanceY / tochRang))/25 * 255;
                            //最终亮度 = 原来的 + 改变亮度；
                            FinalBrightness = (int) Math.min(Math.max(screenBrightness + delta1, 0), 255);
                            if (delta1 != 0) {
                                setScreenBrightness(FinalBrightness);
                            }
                            seekbar_Light.setProgress(FinalBrightness);
                            screenBrightness = FinalBrightness; }
                }else { //触摸面积大于屏幕宽度的一半的位置（右边屏幕）
                    //向上滑动时的操作
                    if (distanceY > 0 && (Math.abs(distanceY) > 100) && endY>400) {
                        Toast.makeText(this, "向上滑动", Toast.LENGTH_SHORT).show();
                        Log.v("向上滑动", String.valueOf(distanceY));
                        LL_Volume.setVisibility(View.VISIBLE);
                        LL_Brightness.setVisibility(View.GONE);
                        //改变声音 = （滑动屏幕的距离： 总距离）*音量最大值
                        float delta = (distanceY / tochRang) * maxVoice;
                        //最终声音 = 原来的 + 改变声音；
                        FinalVolume = (int) Math.min(Math.max(mVol + delta, 0), maxVoice);
                        if (delta != 0) {
                            isMute = false;
                            updataVoice(FinalVolume, isMute);
                        }
                    }else //向下滑动时的操作
                        if (distanceY < 0 && (Math.abs(distanceY) > 100) && endY>400) {
                            Toast.makeText(this, "向下滑动", Toast.LENGTH_SHORT).show();
                            Log.v("向下滑動", String.valueOf(distanceY));
                            LL_Volume.setVisibility(View.VISIBLE);
                            LL_Brightness.setVisibility(View.GONE);
                            //改变声音 = （滑动屏幕的距离： 总距离）*音量最大值
                            float delta = (distanceY / tochRang) * maxVoice;
                            //最终声音 = 原来的 + 改变声音；
                            FinalVolume = (int) Math.min(Math.max(mVol + delta, 0), maxVoice);
                            if (delta != 0) {
                                isMute = false;
                                updataVoice(FinalVolume, isMute); }
                        }
                }

                //左右滑动改变视频的进度
                if (distanceX > 0 && (Math.abs(distanceX) > 150)){
                    Toast.makeText(this, "向右滑动", Toast.LENGTH_SHORT).show();
                    Log.v("向右滑动", String.valueOf(distanceX));
                    float delta2 = (distanceX / screenWidth)/100 * duration;
                    int currentPosition = (int) videoview.getCurrentPosition();
                    FinalProgress = (int) Math.min(Math.max( currentPosition + delta2, 0), duration);
                    videoview.seekTo(FinalProgress);
                }else
                if (distanceX < 0 && (Math.abs(distanceX) >150)) {
                    Toast.makeText(this, "向左滑动", Toast.LENGTH_SHORT).show();
                    Log.v("向左滑动", String.valueOf(distanceX));
                    float delta2 = (distanceX / screenWidth)/100 * duration;
                    int currentPosition = (int) videoview.getCurrentPosition();
                    FinalProgress = (int) Math.min(Math.max( currentPosition + delta2, 0), duration);
                    videoview.seekTo(FinalProgress);
                }

                break;

            case MotionEvent.ACTION_UP ://手指离开
                handler.sendEmptyMessageDelayed(3, 3000);//发消息隐藏LL_Volume和LL_Brightness
                handler.sendEmptyMessageDelayed(2, 3000);//发消息隐藏控制面板
                break;

        }
        return super.onTouchEvent(event);
    }


    //监听物理键实现声音大小的变化
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_VOLUME_DOWN){
            LL_Volume.setVisibility(View.VISIBLE);
            currentVoice --;
            updataVoice(currentVoice,false);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.sendEmptyMessageDelayed(2,3000);
            handler.sendEmptyMessageDelayed(3,2000);
            return true;
        }else if (keyCode ==KeyEvent.KEYCODE_VOLUME_UP){
            LL_Volume.setVisibility(View.VISIBLE);
            currentVoice ++;
            updataVoice(currentVoice,false);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.sendEmptyMessageDelayed(2,3000);
            handler.sendEmptyMessageDelayed(3,2000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Activity的生命周期的销毁方法
    @Override
    protected void onDestroy() {
        //移除所有的消息
        handler.removeCallbacksAndMessages(null);
        //释放资源的时候要写在super的前面，先释放子类，再释放父类
        if (receiver != null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }


}
