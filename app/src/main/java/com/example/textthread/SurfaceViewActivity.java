package com.example.textthread;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

import static android.support.constraint.Constraints.TAG;
import static java.security.AccessController.getContext;

public class SurfaceViewActivity extends Activity {
    @BindView(R.id.bnt_pause)
    Button bntPause;
    SurfaceView surfaceView;
    private IjkMediaPlayer ijkMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        ButterKnife.bind(this);
        surfaceView = findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(callback);//SurfaceView设置回调


//        ijkMediaPlayer.pause();//暂停
//        ijkMediaPlayer.start();//开始播放
//        ijkMediaPlayer.isPlaying();//是否在播放
//        ijkMediaPlayer.stop();//停止播放
//        ijkMediaPlayer.seekTo(10);//
//        ijkMediaPlayer.getCurrentPosition();//当前的播放进度
//        ijkMediaPlayer.getDuration();//获取视频的总时间
//        ijkMediaPlayer.setSpeed(2.0f);//设置播放速度
//        ijkMediaPlayer.getSpeed(2.0f);//获取播放速度
//        ijkMediaPlayer.setDataSource();// 设置播放链接
//        ijkMediaPlayer.setLooping(true);//设置是否循环播放
//         ijkMediaPlayer.getVideoWidth();//获取视频的宽度
//         ijkMediaPlayer.getVideoHeight();//获取视频的长度
//         ijkMediaPlayer.setVolume(1.0f,1.0f);//音量设置范围：0.0f-1.0f，0.0f为静音。
//         ijkMediaPlayer.setScreenOnWhilePlaying(true);//设置屏幕常亮
//         ijkMediaPlayer.reset();//重置MediaPlayer进入未初始化状态
//         ijkMediaPlayer.release();//释放媒体资源
//         ijkMediaPlayer.getTcpSpeed();//获取下载速度
//         ijkMediaPlayer.setWakeMode();//设置此MediaPlayer的低级电源管理行为
//        ijkMediaPlayer.setSurface()和ijkMediaPlayer.setDisplay()是ijkplayer设置视频显示视图的接口

        /*-----------------------------------ijkMediaPlayer的监听--------------------------------------------*/
//         ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IMediaPlayer iMediaPlayer) {
//
//            }
//        });//播放器异步准备监听

//        ijkMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                return false;
//            }
//        });//播放出错


//        ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//
//            }
//        });//播放完毕的监听

//        ijkMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
//            @Override
//            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
//
//            }
//        });//视频宽高值变化监听。客户端可以在该监听接口的回调函数获取片源的宽和高。

//        ijkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
//            @Override
//            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
//
//            }
//        });//缓冲更新。客户端可以在该监听接口的回调函数获取缓冲完成的百分比

//        ijkMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
//                return false;
//            }
//        });//客户端可以在该监听接口的回调函数对播放器上报的报警等其他信息进行相应处理

//        ijkMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
//
//            }
//        });//定位结束监听器

//        ijkMediaPlayer.setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
//            @Override
//            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
//
//            }
//        });//字幕监听接口

//        ijkMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
//
//            }
//        });//拖拽完成监听。客户端可以在该监听接口的回调函数获取缓冲完成的百分比

    }



    //surfaceView的回调方法
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
//当surface第一次创建的时候，这个方法就会被立即调用。这个方法的实现可以完成surface创建后的一些初始化工作。
        public void surfaceCreated(SurfaceHolder holder) {
            createPlayer();//创建播放器，并初始化
            ijkMediaPlayer.setDisplay(surfaceView.getHolder());//设置视频显示视图

            //应用切换至后台时调用播放器pause挂起播放
            if (ijkMediaPlayer!= null)
            {
                ijkMediaPlayer.pause();
            }
        }

        @Override

//当surface的任何结构（格式或大小）发生改变，这个方法就立即被调用。你应该在此刻更新surface。这个方法至少会被调用一次，在surfaceCreated(SurfaceHolder).调用之后。
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //应用切换至前台时调用播放器start继续播放。
            if (ijkMediaPlayer != null)
            {
                ijkMediaPlayer.start();
            }

        }

        @Override
//在一个surface被销毁前，这个方法会被调用。在这个调用返回后，你再也不应该去访问surface了。
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (surfaceView != null) {
                surfaceView.getHolder().removeCallback(callback);
                surfaceView = null;
            }
        }
    };

    private void createPlayer() {
        if (ijkMediaPlayer == null) {
            ijkMediaPlayer = new IjkMediaPlayer();

            //设置解码方式：硬解码 参考网址：https://www.jianshu.com/p/843c86a9e9ad
//            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
//            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);



            //IjkPlayer播放器秒开优化以及常用Option设置
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);//设置播放前的探测时间 1,达到首屏秒开效果
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration",
                    3000);// 最大缓存大小是3秒，可以根据实际需求进行修改。
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);//无限读。
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);//关闭播放器缓冲。
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"framedrop",5);//跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步



            try {

                ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ijkMediaPlayer.setScreenOnWhilePlaying(true);//设置屏幕常亮
                //设置播放地址
                ijkMediaPlayer.setDataSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4");
                ijkMediaPlayer.prepareAsync();//异步准备播放

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    //释放资源
    protected void onDestroy() {
        super.onDestroy();
        release();//释放ijkMediaPlayer的资源
    }


    //释放ijkMediaPlayer的资源
    private void release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
        IjkMediaPlayer.native_profileEnd();
    }

    //按钮的点击事件
    boolean biaoji = true;
    @OnClick({R.id.bnt_pause, R.id.bnt_Speed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnt_pause:

                if (biaoji){
                    ijkMediaPlayer.pause();//暂停
                    biaoji =false;
                }else {
                    ijkMediaPlayer.start();
                    biaoji =true;
                }
                break;
            case R.id.bnt_Speed:
                ijkMediaPlayer.setSpeed(2.0f);//播放速度
                break;
        }
    }
}
