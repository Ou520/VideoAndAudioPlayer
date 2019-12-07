package com.example.textthread;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.textthread.util.FitScreenUtil;
import com.example.textthread.util.MyUtils;




public class VideoPlayerActivity extends AppCompatActivity {
    private boolean backPressed;
    private VideoView videoView;
    private TextView tvVideoInfo;
    private TextView tvPlayTime;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar seekBarVideo;
    private SeekBar seekBarSound;
    private SeekBar seekBarLight;
    private int screenBrightness;
    private int screenMode;
    private AudioManager audioManager;
    private int soundAudio;
    private int soundMode;
    private Button bntStart,bntLL;
    LinearLayout LL;

    String Url="http://www.jmzsjy.com/UploadFile/%E5%BE%AE%E8%AF%BE/%E5%9C%B0%E6%96%B9%E9%A3%8E%E5%91%B3%E5%B0%8F%E5%90%83%E2%80%94%E2%80%94%E5%AE%AB%E5%BB%B7%E9%A6%99%E9%85%A5%E7%89%9B%E8%82%89%E9%A5%BC.mp4";
    //菜单按钮
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横屏和竖屏显示不同的布局
        int orientation=getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE)//横屏
        {
            setContentView(R.layout.activity_video_player2);

            //隐藏状态栏和导航栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }else if (orientation==Configuration.ORIENTATION_PORTRAIT)//竖屏
        {
            setContentView(R.layout.activity_video_player);

            //隐藏状态栏和导航栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }



        String title = "title_activity_player";

        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        soundAudio = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundMode = audioManager.getMode();

        initView();
        initData();

        //菜单的监听
        bntLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断按钮是否重复点击
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {//点击一次
                    // do something
                    LL.setVisibility(View.VISIBLE);
                    mLastClickTime = nowTime;
                } else {//重复点击
                    LL.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    //初始化UI空间
    private void initView() {


        tvVideoInfo = (TextView) findViewById(R.id.tvVideoInfo);
        videoView = findViewById(R.id.videoView);
        tvPlayTime = (TextView) findViewById(R.id.tvPlayTime);

        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        seekBarVideo = (SeekBar) findViewById(R.id.seekBarVideo);

        seekBarSound = (SeekBar) findViewById(R.id.seekBarSound);
        seekBarLight = (SeekBar) findViewById(R.id.seekBarLight);

        bntStart=findViewById(R.id.Start);

        LL=findViewById(R.id.LL);
        bntLL=findViewById(R.id.bntLL);


    }

    @SuppressLint("HandlerLeak")
    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (videoView.getDuration() > 0) {
                        seekBarVideo.setMax(videoView.getDuration());
                        seekBarVideo.setProgress(videoView.getCurrentPosition());
                    }
                    updateTextViewWithTimeFormat(tvCurrentTime, videoView.getCurrentPosition() / 1000);
                    updateTextViewWithTimeFormat(tvTotalTime, videoView.getDuration() / 1000);
                    tvVideoInfo.setText("duration:" + videoView.getDuration() + "\n");
                    tvVideoInfo.append("currentPosition:" + videoView.getCurrentPosition() + "\n");
                    updateTextViewWithTimeFormat(tvPlayTime, videoView.getCurrentPosition() / 1000);
                    uiHandler.sendEmptyMessageDelayed(0, 200);
                    break;
            }
        }
    };

    //初始化数据
    private void initData() {
        videoView.setVideoPath(Url);//视频播放的地址
        //播放视频
        bntStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });


        int screenWidth = MyUtils.getScreenWidth(this);
        int height = screenWidth / 4 * 3;

        FitScreenUtil.FixScreenXY(videoView, screenWidth, height);
        uiHandler.sendEmptyMessageDelayed(0, 200);

        tvVideoInfo.setText("duration:" + videoView.getDuration() + "\n");
        tvVideoInfo.append("currentPosition:" + videoView.getCurrentPosition() + "\n");

        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekPos = seekBar.getProgress();
                videoView.seekTo(seekPos);
            }
        });

        seekBarLight.setMax(255);
        seekBarLight.setProgress(screenBrightness);
        if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
        seekBarLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekPos = seekBar.getProgress();
                setScreenBrightness(seekPos);
            }
        });
        seekBarSound.setMax(15);
        seekBarSound.setProgress(soundAudio);
        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekPos = seekBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekPos, AudioManager.FLAG_PLAY_SOUND);
            }
        });
    }

//    屏幕亮度调节模式
    private void setScreenMode(int value) {
        ContentResolver contentResolver =this.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                try {
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }catch (Exception e)
                {
                    Toast.makeText(this, "setScreenMode有问题！！", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

//    设置当前窗口亮度
    private void setScreenBrightness(int value) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = value / 255.0f;
        window.setAttributes(lp);


    }


    @Override
    public void onBackPressed() {
        backPressed = true;
        setScreenBrightness(screenBrightness);
        setScreenMode(screenMode);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, soundAudio, AudioManager.FLAG_PLAY_SOUND);
        super.onBackPressed();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnForword:
                Toast.makeText(this, "你点击了上一个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStop:
                videoView.pause();
                break;
            case R.id.btnGoOn:
                videoView.start();
                break;
            case R.id.btnNext:
                Toast.makeText(this, "你点击了下一个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnQuickBack:
                videoView.seekTo(videoView.getCurrentPosition() - 1000 * 10);
                break;
            case R.id.btnQuickForword:
                videoView.seekTo(videoView.getCurrentPosition() + 1000 * 10);
                break;
            case R.id.btnGetCurrent:
                tvVideoInfo.append("当前进度：" + videoView.getCurrentPosition() + "\n");
                tvVideoInfo.append("duration:" + videoView.getDuration() + "\n");
                tvVideoInfo.append("currentPosition:" + videoView.getCurrentPosition() + "\n");
                break;
            case R.id.bntLL1:

                 //  强制设置为横屏
                     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                break;
            case R.id.bntLL2:
               //强制竖屏设置
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                break;
        }
    }

    private void updateTextViewWithTimeFormat(TextView textView, int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String stringTemp = null;
        if (0 != hh) {
            stringTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            stringTemp = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(stringTemp);
    }

    @Override
    protected void onResume() {

//        /**
//         * 强制竖屏设置
//         */
//        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

//        /**
//         * 强制设置为横屏
//         */
//        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }

        super.onResume();
        if (videoView.getCurrentPosition() > 0) {
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backPressed ) {
            videoView.stopPlayback();
            videoView.stopPlayback();
        } else {
            videoView.stopPlayback();
        }
        videoView.pause();


        setScreenBrightness(screenBrightness);
        setScreenMode(screenMode);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, soundAudio, AudioManager.FLAG_PLAY_SOUND);
    }

}
