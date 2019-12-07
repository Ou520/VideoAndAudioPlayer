package com.example.textthread.TabLayout.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.textthread.IMusicPlayerService;
import com.example.textthread.R;
import com.example.textthread.TabLayout.service.MusicPlayerService;
import com.example.textthread.TabLayout.utils.LyricUtils;
import com.example.textthread.TabLayout.utils.Utils;
import com.example.textthread.TabLayout.view.ShowLyricView;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemAudioPlayer extends AppCompatActivity {
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar_audio)
    SeekBar seekbarAudio;
    @BindView(R.id.bnt_audio_mode)
    Button bntAudioMode;
    @BindView(R.id.bnt_audio_pre)
    Button bntAudioPre;
    @BindView(R.id.bnt_audio_start_pause)
    Button bntAudioStartPause;
    @BindView(R.id.bnt_audio_next)
    Button bntAudioNext;
    @BindView(R.id.bnt_lyrc)
    Button bntLyrc;
    @BindView(R.id.showLyricView)
    ShowLyricView showLyricView;


    private int position;//点击列表item的位置
    private IMusicPlayerService service;//服务的代理类
    private MyReceiver receiver;//广播
    private static final int PROGRESS = 1;//进度更新
    private static final int SHOW_LYRIC = 2;//显示歌词
    private Utils utils;//定义工具类
    private boolean notification;//（状态栏通知的标记）true:从状态栏进入（不需要重新播放），false:从播放列表进入


    //音频播放服务的回调
    ServiceConnection con = new ServiceConnection() {
        @Override
        //当连接成功的时候回调这个方法
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    if (!notification) {
                        //从列表
                        service.openAudio(position);
                    } else {
                        //从状态栏
                        showViewData();//重新设置视图的数据（如果不是主线程会有问题）
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        //当断连接的时候回调这个方法
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (service != null) {
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    //发消息和接收消息来更新进度
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        //接收消息
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LYRIC: //发消息来更新歌词
                    try {
                        //1.得到当前的进度
                        int currentPosition = service.getCurrentPosition();
                        //2.把进度传入ShowLyricView控件，并且计算该高亮哪一句
                        showLyricView.setshowNextLyric(currentPosition);
                        //3.实时的发消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PROGRESS://发消息来更新进度
                    try {
                        //1.得到当前进度
                        int currentPosition = service.getCurrentPosition();
                        //2.设置SeekBar的进度
                        seekbarAudio.setProgress(currentPosition);
                        //3.设置时间进度
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils.stringForTime(service.getDuration()));
                        //4.每秒更新一次

                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_player);
        ButterKnife.bind(this);
        initData();
        getData();
        setListener();
        bindAndStartService();//创建和启动自定义音频服务


    }


    private void initData() {
        //初始化工具类
        utils = new Utils();
        //注册广播
        receiver = new MyReceiver();//初始化自定义广播
        IntentFilter intentFilter = new IntentFilter();//定义一个意图过滤器，来判断接收是那个广播
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);//赋值意图
        registerReceiver(receiver, intentFilter);//通过携带意图来注册某个特定的广播


    }

    //自定义一个广播（可以用EventBus代替广播，消息等的作用）
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收MusicPlayerService发的广播开始歌词同步
            showLyric();
            showViewData();//设置视图的数据
            checkPlaymode();//校验按钮的状态（因为服务被重复初始化使用失效了）



        }
    }

    //显示歌词的方法
    private void showLyric() {
        //解析歌词的工具类
        LyricUtils lyricUtils = new LyricUtils();
        try {
            //得到歌曲的绝对路径
            String path = service.getAudioPath();
            //通过歌曲的路径来得到歌词的路径（歌词和歌曲放在一起）
            path = path.substring(0,path.lastIndexOf("."));

            //通过路径获取歌词的文件
            File file = new File(path + ".lrc");//歌词文件的格式一
            if(!file.exists()){//如果没有上面格式的文件就去获取下面格式的文件

                file = new File(path + ".txt");//歌词文件的格式二
            }

            lyricUtils.readLyricFile(file);//把获取的歌词文件传给歌词解析类去解析

            showLyricView.setLyrics(lyricUtils.getLyrics());//从歌词解析类里获取解析好的歌词，然后为showLyricView设置歌词

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(lyricUtils.isExistsLyric()){ //如果存在歌词就发消息去更新歌词
            handler.sendEmptyMessage(SHOW_LYRIC);//发消息去更新歌词
        }

    }

    //显示视图的数据的方法
    private void showViewData() {
        try {
            tvArtist.setText("作者：" + service.getArtist());
            tvName.setText("歌曲名：" + service.getName());
            seekbarAudio.setMax(service.getDuration());//设置进度条的最大值

            //发消息通知进度更新
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //获取ChannelFragment的Intent传来的数据
    private void getData() {
        notification = getIntent().getBooleanExtra("Notification", false);
        if (!notification) {
            //得到ChannelFragment传来的数据（列表item的位置）
            position = getIntent().getIntExtra("position", 0);
        }

    }

    //设置监听的方法
    private void setListener() {
        //设置SeekBar的监听
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }


    //SeekBar的监听的方法
    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    //创建和启动自定义的音频服务的方法
    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.Ouwenbin.mobileplayer_OPENAUDIO");
        bindService(intent, con, BIND_AUTO_CREATE);//通过绑定Activity的方式启动服务
        startService(intent);//防止重复实例化多个服务（要去AndroidManifest.xml文件中个这个服务设置：android:enabled="true"   android:exported="false"）
    }


    //音频播放面板按钮的点击事件
    @OnClick({R.id.bnt_audio_mode, R.id.bnt_audio_pre, R.id.bnt_audio_start_pause, R.id.bnt_audio_next, R.id.bnt_lyrc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnt_audio_mode://播放模式的按钮
                setPlaymode();
                break;
            case R.id.bnt_audio_pre://播放上一个的按钮
                try {
                    service.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bnt_audio_start_pause://暂停按钮
                if (service != null) {
                    try {
                        if (service.isPlaying()) {
                            //暂停
                            service.pause();
                            //按钮设置为播放
                            bntAudioStartPause.setBackgroundResource(R.drawable.bnt_audio_start_selector);
                        } else {
                            //播放
                            service.start();
                            //按钮设置为暂停
                            bntAudioStartPause.setBackgroundResource(R.drawable.bnt_audio_pause_selector);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.bnt_audio_next://下一首按钮
                if (service != null) {
                    try {
                        service.next();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.bnt_lyrc://歌词
                break;
        }
    }


    //设置播放器的播放模式的方法
    private void setPlaymode() {
        try {
            //设置播放模式（通过标记）
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                playmode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                playmode = MusicPlayerService.REPEAT_ALL;
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            } else {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }

            //保存设置好的播放模式到服务中
            service.setPlayMode(playmode);

            //设置图片
            showPlaymode();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //设置播放模式按钮的图片和弹出消息
    private void showPlaymode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_single_selector);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_all_selector);
                Toast.makeText(this, "列表循环", Toast.LENGTH_SHORT).show();
            } else {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //校验按钮的状态
    private void checkPlaymode() {

        try {
            int playmode = service.getPlayMode();
            //校验播放模式按的状态
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_normal_selector);
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_single_selector);
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_all_selector);
            } else {
                bntAudioMode.setBackgroundResource(R.drawable.bnt_audio_playmode_normal_selector);
            }

            //校验播放和暂停按钮的状态
            if (service.isPlaying()){
                bntAudioStartPause.setBackgroundResource(R.drawable.bnt_audio_pause_selector);
            }else {
                bntAudioStartPause.setBackgroundResource(R.drawable.bnt_audio_start_selector);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        //移除消息
        handler.removeCallbacksAndMessages(null);
        //取消注册广播
        if (receiver != null) {
            unregisterReceiver(receiver);//取消注册广播
            receiver = null;//设置为null，垃圾回收会优先回收，建议要回收的数据都设为null
        }
        //解绑服务(不解绑不影响软件运行，但是会报一个异常)
        if (con != null) {
            unbindService(con);//解绑服务
        }
        super.onDestroy();
    }
}
