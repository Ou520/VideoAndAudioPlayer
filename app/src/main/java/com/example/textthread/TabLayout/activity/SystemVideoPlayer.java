package com.example.textthread.TabLayout.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.R;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.Utils;
import com.example.textthread.TabLayout.view.CircleColorButton;
import com.example.textthread.TabLayout.view.VideoView;
import com.example.textthread.util.BiliDanmukuParser;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;

import static com.example.textthread.R.id.bnt_Menus;


//系统播放器
public class SystemVideoPlayer extends Activity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_networkType)
    TextView tv_networkType;
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
    @BindView(R.id.ll_Content)
    LinearLayout ll_Content;
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
    @BindView(R.id.bnt_Like)
    Button bntLike;
    @BindView(R.id.bnt_NoLike)
    Button bntNoLike;
    @BindView(R.id.bnt_InsertCoins)
    Button bntInsertCoins;
    @BindView(R.id.bnt_Share)
    Button bntShare;
    @BindView(bnt_Menus)
    Button bntMenus;
    @BindView(R.id.bnt_addDanmaku)
    Button bnt_addDanmaku;
    @BindView(R.id.bnt_DanmakuHideAndShow)
    Button bntDanmakuHideAndShow;
    @BindView(R.id.bnt_DanmakuSet)
    Button bntDanmakuSet;
    @BindView(R.id.rl_SystemVideoPlayer)
    RelativeLayout rlSystemVideoPlayer;

    private Uri uri;
    //视频进度的更新
    private static final int PROGRESS = 1;
    //视频的总时长
    private int duration;
    //工具类
    private Utils utils;
    // 监听电量变化的广播
    private MyReceiver receiver;
    //传入进来的视频列表
    private ArrayList<MediaItem> mediaItems;
    //要播放的列表中的具体位置
    private int position;
    //1.定义手势识别器
    private GestureDetector detector;
    //是否显示控制面板
    private boolean isshowMediaController = false;
    //调节声音
    private AudioManager am;
    //当前的音量(0-15)
    private int currentVoice;
    //最大的音量
    private int maxVoice;
    //当前的亮度（0-255）
    private int screenBrightness;
    //设置是否静音
    private boolean isMute = false;
    //屏幕的宽（x轴的距离）
    private int screenWidth = 0;
    //屏幕的高（y轴的距离）
    private int screenHeight = 0;
    //是否是网络的URI
    private boolean isNetUri;
    //视频卡顿的监听方式(还没用)
    private boolean isUseSysten = false;
    //上一次的视频播放进度
    private int precurrentPosition;
    private MediaItem mediaItem;
    private PopupWindow popupWindow;
    //弹幕
    private IDanmakuView mDanmakuView;//弹幕的视图
    private BaseDanmakuParser mParser;//创建弹幕解析器(弹幕数据源)
    private DanmakuContext mContext;
    private  boolean DanmakuShow = true;//设置是否显示弹幕

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
        private Drawable mDrawable;
        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if(drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable);
                            danmaku.text = spannable;
                            if(mDanmakuView != null) {
                                mDanmakuView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
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
        utils = new Utils();
        //注册电量广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //当电量变化的时候发广播
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(receiver, intentFilter);

        //2.实例化手势识别器，并且重写双击，单击，长按
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
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
                if (isshowMediaController) {
                    //隐藏
                    hideMediaController();
                    //把隐藏消息移除
                    handler.removeMessages(2);
                } else {
                    //显示
                    showMediaController();
                    //发消息通知隐藏控制面板
                    handler.sendEmptyMessageDelayed(2, 3000);
                }

                return super.onSingleTapConfirmed(e);
            }
        });

        //得到屏幕的宽和高
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        screenWidth = outPoint.x;//屏幕x轴的距离（横竖屏切换是会变化）
        screenHeight = outPoint.y;//屏幕y轴的距离（横竖屏切换是会变化）
        //设置视频的大小
//        videoview.setVideoSize(screenHeight, screenWidth);

        //1.得到音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前的音量
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取最大音量
        //获取当前的亮度
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        //开始更新网速
        handler.sendEmptyMessage(3);


    }

    //获取当前系统时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");// yyyy年MM月dd日HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }


    private void getData() {
        //得到播放地址
        uri = getIntent().getData();//数据来自于外部应用
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void setData() {
        //为VideoView设置地址
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//设置视频的名称
            isNetUri = utils.isNetUri(mediaItem.getData());//判断URI是否是网络连接
            videoview.setVideoPath(mediaItem.getData());
            tv_networkType.setVisibility(View.GONE);
        } else if (uri != null) {
            tvName.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            videoview.setVideoURI(uri);
        } else {
            Toast.makeText(this, "地址为空,没有数据！", Toast.LENGTH_SHORT).show();
        }

        //设置按钮的状态
        if (!playmode.equals("REPEAT_ALL")){
            setButtonState();
        }

        //2.设置最大音量和seekBar做关联
        seekbarVoice.setMax(maxVoice);
        //设置亮度的SeekBar
        seekbar_Light.setMax(255);
        seekbar_Light.setProgress(screenBrightness);
        //设置当前的音量
        seekbarVoice.setProgress(currentVoice);
        //设置当前的网络状态
        tv_networkType.setText(utils.getNetworkType(this));

        ll_Content.setVisibility(View.GONE);


        // DanmakuView(弹幕)
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        //创建弹幕控件上下文，类似Context，里面可以进行一系列配置
        mContext = DanmakuContext.create();//设置弹幕显示的各种属性DanmakuContext.java里
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)//设置描边样式
                .setDuplicateMergingEnabled(false)//设置不合并相同内容弹幕
                .setScrollSpeedFactor(1.2f)//设置弹幕滚动速度缩放比例，越大速度越慢
                .setScaleTextSize(1.2f)//设置字体缩放比例
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter)//图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)//设置最大行数策略
                .preventOverlapping(overlappingEnablePair)//设置禁止重叠策略
//                .setDanmakuTransparency(0.2f)//设置弹幕的透明度
                .setDanmakuMargin(40);//外边距
        if (mDanmakuView != null) {
            //设置弹幕数据源，B站是xml
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));//设置弹幕数据

            //替换为A站弹幕数据源，因为A站弹幕数据是json，
//            try {
//                mParser = createParser(this.getAssets().open("comment.json"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            //设置DanmakuView相关回调
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });

            //设置监听
//            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
//
//                @Override
//                //点击弹幕的监听
//                public boolean onDanmakuClick(IDanmakus danmakus) {
//                    Toast.makeText(SystemVideoPlayer.this, "点击了弹幕！", Toast.LENGTH_SHORT).show();
//
//                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
//                    BaseDanmaku latest = danmakus.last();
//                    if (null != latest) {
//                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
//                        return true;
//                    }
//                    return false;
//                }
//
//                @Override
//                //长按弹幕的监听
//                public boolean onDanmakuLongClick(IDanmakus danmakus) {
//                    Toast.makeText(SystemVideoPlayer.this, "长按了弹幕！", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                @Override
//                //点击DanmakuView视图的监听
//                public boolean onViewClick(IDanmakuView view) {
//
//                    Toast.makeText(SystemVideoPlayer.this, "点击了DanmakuView视图", Toast.LENGTH_SHORT).show();
//
////                    mMediaController.setVisibility(View.VISIBLE);//控制
//                    return false;
//                }
//            });
            mDanmakuView.prepare(mParser, mContext);//设置弹幕的数据
//            mDanmakuView.showFPS(true);//设置是否显示Fps
            mDanmakuView.enableDanmakuDrawingCache(true);//提升屏幕绘制效率

        }

    }

    //设置弹幕数据源
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
    //弹幕图文混排
    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");//图片后的位置
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private BaseDanmaku danmaku;
    String TYPE_SCROLL ="TYPE_SCROLL_RL";
    String textSize ="中号";
    //设置弹幕的样式（纯文本）
    private void addDanmaku(boolean islive) {
        //设置弹幕的类型类型(1从右至左滚动弹幕|6从左至右滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
        if (TYPE_SCROLL=="TYPE_SCROLL_RL"){
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        }else if (TYPE_SCROLL=="TYPE_SCROLL_LR"){
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_LR);
        }else if (TYPE_SCROLL=="TYPE_FIX_TOP"){
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_FIX_TOP);
        }else if (TYPE_SCROLL=="TYPE_FIX_BOTTOM"){
            danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_FIX_BOTTOM);
        }
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        //设置弹幕的属性，在BaseDanmaku.java里可以看见
        danmaku.text =addDanmaku.getText();//文本
        danmaku.padding = 5;//内边距(像素)
        danmaku.priority = 0; //可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;//是否是直播弹幕
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);//显示时间(毫秒)

        if (textSize =="中号"){
            danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);//设置文本的字体大小()
        }else if (textSize =="小号"){
            danmaku.textSize = 15f * (mParser.getDisplayer().getDensity() - 0.6f);//设置文本的字体大小()
        }else if (textSize =="大号"){
            danmaku.textSize = 45f * (mParser.getDisplayer().getDensity() - 0.6f);//设置文本的字体大小()
        }

        danmaku.textColor = colorButton.getColor();//弹幕文字颜色
//        danmaku.textShadowColor = Color.RED;//设置阴影/描边颜色
         danmaku.underlineColor = Color.GREEN;//下划线颜色,0表示无下划线
//        danmaku.borderColor = Color.GREEN;//弹幕文字边框的颜色
//        danmaku.userHash ="";//设置用户名
//        danmaku.visibility =BaseDanmaku.VISIBLE;//是否可见(INVISIBLE)

        mDanmakuView.addDanmaku(danmaku);//添加一条弹幕


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
        //控制面板按钮的监听
        bntMenus.setOnClickListener(new MybntMenusClickListener());
        bntDanmakuSet.setOnClickListener(new MybntMenusClickListener());
        bnt_addDanmaku.setOnClickListener(new MybntMenusClickListener());

    }


    //接收消息，并进行相应的操作
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2://3秒
                    hideMediaController();//隐藏控制面板
                    break;
                case PROGRESS:
                    //1.得到当前的播放进度
                    int currentPosition = videoview.getCurrentPosition();
                    //2.给SeekBar设置当前视频进度
                    seekbarVideo.setProgress(currentPosition);
                    //更新播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition)+" / "+utils.stringForTime(duration));
                    //设置系统时间
                    String time = getSystemTime();
                    tvSystemTime.setText(time);

                    //缓存进度的更新
                    if (isNetUri) {
                        //只有网络资源才有缓存效果
                        int buffer = videoview.getBufferPercentage();//缓存是0-100的值
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);

                    } else {
                        //本地视频没有缓存效果
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //监听网络视频播放的卡顿(直播时不能用)
                    /*
                        if(!isUseSysten){
                         if (videoview.isPlaying()){
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
                        }

                     */

                    //3.每秒更新一次
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                case 3://2秒
                    //1.得到网速
                    String netSpeed = utils.getNetSpeed(SystemVideoPlayer.this);
                    //显示网速
                    tv_loading_netSpeed.setText("玩命加载中..." + netSpeed);
                    tv_buffer_netSpeed.setText("缓存中..." + netSpeed);
                    //每两秒调用一次
                    handler.removeMessages(3);
                    handler.sendEmptyMessageDelayed(3, 2000);
                    //隐藏布局
                    LL_Volume.setVisibility(View.GONE);
                    LL_Brightness.setVisibility(View.GONE);
                    break;
            }
        }
    };


    //通过广播来获取系统电量的变化情况，并设置电池图标的变化
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//0-100
            setBattery(level);//设置电池图片的变化
        }
    }

    //设置电池图标的状态
    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
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


    /*-----------------------------------------------------监听事件开始--------------------------------------------------------------------------------------*/

    ////设置视频的控制面板的按钮的点击事件
    @OnClick({R.id.bnt_DanmakuHideAndShow,R.id.bnt_voice, R.id.bnt_exit, R.id.bnt_Like, R.id.bnt_NoLike, R.id.bnt_InsertCoins, R.id.bnt_Share, bnt_Menus, R.id.bnt_video_pre, R.id.bnt_video_start_pause, R.id.bnt_video_next, R.id.bnt_video_siwch_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnt_Like:
                Toast.makeText(this, "点击了点赞按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_NoLike:
                Toast.makeText(this, "点击了不点赞按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_InsertCoins:
                Toast.makeText(this, "点击了投币按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_Share:
                Toast.makeText(this, "点击了分享按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_DanmakuHideAndShow:
                DanmakuHideAndShow();
                break;
            case R.id.bnt_voice:
                isMute = !isMute;
                updataVoice(currentVoice, isMute);//设置静音
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
                    ll_Content.setVisibility(View.VISIBLE);
                } else if (orientation == mConfiguration.ORIENTATION_PORTRAIT) {
                    //竖屏时
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                    bntVideoSiwchScreen.setBackgroundResource(R.drawable.bnt_video_siwch_screen_default_selector);
                    ll_Content.setVisibility(View.GONE);
                }

                break;
        }
        handler.removeMessages(2);
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    private void DanmakuHideAndShow() {
        if (DanmakuShow){
            mDanmakuView.hide();
            DanmakuShow=false;
            bntDanmakuHideAndShow.setBackgroundResource(R.drawable.ic_danmukai);
        }else {
            mDanmakuView.show();
            DanmakuShow=true;
            bntDanmakuHideAndShow.setBackgroundResource(R.drawable.ic_danmuguang);
        }

    }

    /*--------------------------------------------------控制面板按钮的操作的方法----------------------------------------------------------------------------------*/
    //播放和暂停
    private void StarAndPause() {
        if (videoview.isPlaying()) {
            //视频在播放-设置为暂停
            videoview.pause();
            //按钮状态设置为播放
            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_video_start_selector);

            mDanmakuView.pause();//暂停弹幕
        } else {
            //视频的播放
            videoview.start();
            //按钮的状态设置为暂停
            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
            //播放弹幕
            mDanmakuView.resume();
        }
    }

    //播放上一个视频
    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (playmode =="REPEAT_ALL"){
                if (position <=-1){
                    position=mediaItems.size();
                }
                //播放上一个
                position--;
                if (position >= 0) {
                    MediaItem mediaItem = mediaItems.get(position);
                    tvName.setText(mediaItem.getName());
                    isNetUri = utils.isNetUri(mediaItem.getData());
                    videoview.setVideoPath(mediaItem.getData());

                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                    ll_loading.setVisibility(View.VISIBLE);
                }
            }else {
                //播放上一个
                position--;
                if (position >= 0) {
                    MediaItem mediaItem = mediaItems.get(position);
                    tvName.setText(mediaItem.getName());
                    isNetUri = utils.isNetUri(mediaItem.getData());
                    videoview.setVideoPath(mediaItem.getData());
                    //设置按钮状态
                    setButtonState();

                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                    ll_loading.setVisibility(View.VISIBLE);
                }

            }
        } else {
            setButtonState();
        }

    }

    //播放下一个视频
    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (playmode =="REPEAT_ALL"){
                if (position==mediaItems.size()-1){
                    position=-1;
                }
                position++;
                if (position < mediaItems.size()) {
                    MediaItem mediaItem = mediaItems.get(position);
                    tvName.setText(mediaItem.getName());
                    isNetUri = utils.isNetUri(mediaItem.getData());
                    videoview.setVideoPath(mediaItem.getData());

                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                    ll_loading.setVisibility(View.VISIBLE);
                }

            }else {
                //播放下一个
                position++;
                if (position < mediaItems.size()) {
                    MediaItem mediaItem = mediaItems.get(position);
                    tvName.setText(mediaItem.getName());
                    isNetUri = utils.isNetUri(mediaItem.getData());
                    videoview.setVideoPath(mediaItem.getData());
                    //设置按钮状态
                    setButtonState();
                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                    ll_loading.setVisibility(View.VISIBLE);
                }
            }

        } else {
            setButtonState();
        }
    }

//    按钮的状态方法
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


    //声音大小的SeekBar的拖动监听
    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (progress > 0) {
                    isMute = false;
                } else {
                    isMute = true;
                }
                updataVoice(progress, isMute);//更新音量变化
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
            handler.sendEmptyMessageDelayed(2, 3000);
        }
    }

    //更新音量变化（设置音量的大小）
    private void updataVoice(int progress, boolean isMute) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
            //设置声音按钮的状态
            if (seekbarVoice.getProgress() == 0) {
                bntVoice.setBackgroundResource(R.drawable.btn_voice_pressed);
            } else {
                bntVoice.setBackgroundResource(R.drawable.btn_voice_normal);
            }

        } else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
            //设置声音按钮的状态
            if (seekbarVoice.getProgress() == 0) {
                bntVoice.setBackgroundResource(R.drawable.btn_voice_pressed);
            } else {
                bntVoice.setBackgroundResource(R.drawable.btn_voice_normal);
            }
        }

    }

    //亮度大小的SeekBar的拖动监听
    class LightOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }
/**/
        //当手指触碰的时候回调这个方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        //当手指离开的时候回调这个方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int seekPos = seekBar.getProgress();
            setScreenBrightness(seekPos);
            handler.sendEmptyMessageDelayed(2, 3000);
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
    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        //当手指滑动的时候，会引起SeekBar进度变化时回调这个方法
        //fromUser：如果是用户引起的返回true，不是用户引起的返回false
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser == true) {
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
            handler.sendEmptyMessageDelayed(2, 3000);
        }
    }

    //菜单和弹幕设置按钮的监听
    private class MybntMenusClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bnt_Menus:
                    handler.removeMessages(2);
                    showNoneEffect("Menus");//设置弹窗
                    break;
                case R.id.bnt_DanmakuSet:
                    handler.removeMessages(2);
                    showNoneEffect("DanmakuSet");//设置弹窗
                    break;
                case R.id.bnt_addDanmaku:
                    handler.sendEmptyMessageDelayed(2, 1);//隐藏控制面板
                    showNoneEffect("addDanmaku");//设置弹窗
                    break;
            }

        }
    }

    EditText addDanmaku;
    CircleColorButton colorButton;
    RadioGroup rg_Size;
    RadioGroup rg_Location;
    RadioGroup rg_Color;
    Button bnt_Show;
    //设置弹窗
    private void showNoneEffect(String Tag) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vPopupWindow;
        if (Tag =="Menus"){
             vPopupWindow = inflater.inflate(R.layout.popupwindow_layout, null, false);//引入弹窗布局
            //绑定自定义样式布局里的控件

            //定义PopupWindow视图(设置popupWindow的大小)
            //PopupWindow(View contentView, int width, int height, boolean focusable)
            //ActionBar.LayoutParams.WRAP_CONTENT,(根据内容来决定大小)
            //ActionBar.LayoutParams.MATCH_PARENT（为铺满）
            popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);

        }else if (Tag =="DanmakuSet"){
            vPopupWindow = inflater.inflate(R.layout.popupwindow_danmakuset_layout, null, false);//引入弹窗布局
            //定义PopupWindow视图(设置popupWindow的大小)
            //PopupWindow(View contentView, int width, int height, boolean focusable)
            //ActionBar.LayoutParams.WRAP_CONTENT,(根据内容来决定大小)
            //ActionBar.LayoutParams.MATCH_PARENT（为铺满）
            popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);

        }else if (Tag =="addDanmaku"){
            mDanmakuView.pause();//暂停弹幕
            videoview.pause();//暂停视频

            vPopupWindow = inflater.inflate(R.layout.popupwindow_add_danmaku_layout, null, false);//引入弹窗布局
            //定义PopupWindow视图(设置popupWindow的大小)
            //PopupWindow(View contentView, int width, int height, boolean focusable)
            //ActionBar.LayoutParams.WRAP_CONTENT,(根据内容来决定大小)
            //ActionBar.LayoutParams.MATCH_PARENT（为铺满）
            popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
            //绑定按钮
            Button closePopupwindow = vPopupWindow.findViewById(R.id.close_Popupwindow);
            Button sendDanmaku = vPopupWindow.findViewById(R.id.bnt_sendDanmaku);
            addDanmaku = vPopupWindow.findViewById(R.id.et_addDanmaku);
            colorButton =vPopupWindow.findViewById(R.id.iv_color);
            colorButton.setColor(Color.WHITE);//设置colorButton的颜色

            RadioButton rb_SmallSize =vPopupWindow.findViewById(R.id.rb_SmallSize);
            RadioButton rb_BigSize =vPopupWindow.findViewById(R.id.rb_BigSize);
            RadioButton rb_Top =vPopupWindow.findViewById(R.id.rb_Top);
            RadioButton rb_LR =vPopupWindow.findViewById(R.id.rb_LR);
            RadioButton rb_Bottom =vPopupWindow.findViewById(R.id.rb_Bottom);
            bnt_Show =vPopupWindow.findViewById(R.id.bnt_Show);
//            //颜色的单选按钮
            RadioButton rb_White =vPopupWindow.findViewById(R.id.rb_White);
            RadioButton rb_Red =vPopupWindow.findViewById(R.id.rb_Red);
            RadioButton rb_Yellow =vPopupWindow.findViewById(R.id.rb_Yellow);
            RadioButton rb_Green =vPopupWindow.findViewById(R.id.rb_Green);
            RadioButton rb_Blue =vPopupWindow.findViewById(R.id.rb_Blue);
            RadioButton rb_Pink =vPopupWindow.findViewById(R.id.rb_Pink);
            RadioButton rb_LightGreen =vPopupWindow.findViewById(R.id.rb_LightGreen);
            RadioButton rb_DarkBlue =vPopupWindow.findViewById(R.id.rb_DarkBlue);
            RadioButton rb_Gold =vPopupWindow.findViewById(R.id.rb_Gold);
            RadioButton rb_Purple =vPopupWindow.findViewById(R.id.rb_Purple);
            RadioButton rb_LightBLue =vPopupWindow.findViewById(R.id.rb_LightBLue);
            RadioButton rb_Olive =vPopupWindow.findViewById(R.id.rb_Olive);

            rg_Size =vPopupWindow.findViewById(R.id.rg_Size);
            rg_Location =vPopupWindow.findViewById(R.id.rg_Location);
            rg_Color =vPopupWindow.findViewById(R.id.rg_Color);

            //为布局里的按钮设置监听（其他控件的操作类）
            closePopupwindow.setOnClickListener(new MyPopupwindowbntClickListener());
            sendDanmaku.setOnClickListener(new MyPopupwindowbntClickListener());
            bnt_Show .setOnClickListener(new MyPopupwindowbntClickListener());
            rb_SmallSize.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_BigSize.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Top.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_LR.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Bottom.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_White.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Red.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Yellow.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Green.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Blue.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Pink.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_LightGreen.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_DarkBlue.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Gold.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Purple.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_LightBLue.setOnCheckedChangeListener(new MyCheckedChangeListener());
            rb_Olive.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }


        //设置进出动画(从底部向上进入，从上部向下退出)
        if (Tag =="addDanmaku"){
//            popupWindow.setAnimationStyle(R.style.AddDanmaku_PopupWindowAnimation);
        }else {
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        }

        //关闭PopupWindow后的操作
        popupWindow.setOnDismissListener(new MyDismissListener());

        //设置PopupWindow显示的位置（引入（父布局）依附的布局）
        View parentView = LayoutInflater.from(SystemVideoPlayer.this).inflate(R.layout.activity_system_video_player, null);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        if (Tag =="addDanmaku"){
            popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);//方法内有四个参数:
        }else {
            popupWindow.showAtLocation(parentView, Gravity.RIGHT, 0, 0);//方法内有四个参数:
        }

        // partent：个人理解是要展现在哪个布局的上面,
        // Gravity:TOP(页面顶部),BOTTOM(页面底部),CENTER(页面中心),LEFT(页面左侧中间),RIGHT(页面右侧中间);Gravity.LEFT|Gravity.BOTTOM，表示页面左下角
        //x：x轴偏移量，正数向右偏移，负数向左偏移
        //y：y轴偏移量，正数向下偏移，负数向上偏移
//         */
        //相对于按钮
//        int width = bntMenus.getMeasuredWidth();//父按钮宽度
//        int p_width = popupWindow.getWidth();//popup宽度
//        popupWindow.showAsDropDown(bntMenus,(width/2)-(p_width/2),1);//表示在父控件的四周显示，位置要自己计算
    }

/*---------------------------------PopupWindow里布局对应的监听-------------------------------------------------------*/
    //关闭PopupWindow后的操作的监听
    class MyDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            mDanmakuView.resume();//播放弹幕
            videoview.start();//播放视频
            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
            TYPE_SCROLL="TYPE_SCROLL_RL";//设置弹幕的位置为默认
            textSize ="中号";//设置弹幕的字体大小为默认
            colorButton.setColor(Color.WHITE);//设置弹幕的字体颜色为默认
            handler.sendEmptyMessageDelayed(2, 1);//隐藏控制面板
        }
    }
    //Popupwindow里按钮的监听
    boolean RadioGroupShow=true;
    class MyPopupwindowbntClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.close_Popupwindow:
                    popupWindow.dismiss();
                    break;
                case R.id.bnt_sendDanmaku:
                    addDanmaku(false);
                    popupWindow.dismiss();
                    break;
                case R.id.bnt_Show:
                    if (RadioGroupShow){
                        rg_Size.setVisibility(View.GONE);
                        rg_Location.setVisibility(View.GONE);
                        rg_Color.setVisibility(View.VISIBLE);
                        bnt_Show.setBackgroundResource(R.drawable.ic_jianto2);
                        RadioGroupShow=false;
                    }else {
                        rg_Size.setVisibility(View.VISIBLE);
                        rg_Location.setVisibility(View.VISIBLE);
                        rg_Color.setVisibility(View.GONE);
                        bnt_Show.setBackgroundResource(R.drawable.ic_jianto);
                        RadioGroupShow=true;
                    }
                    break;
            }
        }
    }
    //Popupwindow里单选按钮的监听
    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId()==R.id.rb_SmallSize &&isChecked){
                    textSize ="小号";
             }else if (buttonView.getId()==R.id.rb_BigSize &&isChecked){
                    textSize ="大号";
             }else if (buttonView.getId()==R.id.rb_Top &&isChecked){
                     TYPE_SCROLL="TYPE_FIX_TOP";
             }else if(buttonView.getId()==R.id.rb_LR &&isChecked){
                     TYPE_SCROLL="TYPE_SCROLL_LR";
             }else if(buttonView.getId()==R.id.rb_Bottom &&isChecked){
                     TYPE_SCROLL="TYPE_FIX_BOTTOM";

             }else if(buttonView.getId()==R.id.rb_White &&isChecked){
                colorButton.setColor(Color.rgb(255,255,255));
            }else if(buttonView.getId()==R.id.rb_Red &&isChecked){
                colorButton.setColor(Color.rgb(	255,0,0));
            }
            else if(buttonView.getId()==R.id.rb_Yellow &&isChecked){
                colorButton.setColor(Color.rgb(255,255,0));
            }
            else if(buttonView.getId()==R.id.rb_Green &&isChecked){
                colorButton.setColor(Color.rgb(0,128,0));
            }
            else if(buttonView.getId()==R.id.rb_Blue &&isChecked){
                 colorButton.setColor(Color.rgb(0,191,255));
            }
            else if(buttonView.getId()==R.id.rb_Pink &&isChecked){
                colorButton.setColor(Color.rgb(	255,192,203));
            }
            else if(buttonView.getId()==R.id.rb_LightGreen &&isChecked){
                colorButton.setColor(Color.rgb(	144,238,144));
            }
            else if(buttonView.getId()==R.id.rb_DarkBlue &&isChecked){
                colorButton.setColor(Color.rgb(	0,0,139));
            }
            else if(buttonView.getId()==R.id.rb_Gold &&isChecked){
                colorButton.setColor(Color.rgb(	255,215,0));
            }
            else if(buttonView.getId()==R.id.rb_Purple &&isChecked){
                colorButton.setColor(Color.rgb(	128,0,128));
            }
            else if(buttonView.getId()==R.id.rb_LightBLue &&isChecked){
                colorButton.setColor(Color.rgb(	173,216,230));
            }
            else if(buttonView.getId()==R.id.rb_Olive &&isChecked){
                colorButton.setColor(Color.rgb(	128,128,0));
            }
        }
    }


/*------------------------------------------MediaPlayer的监听---------------------------------------------------*/
    //准备播放时的监听（MediaPlayer）
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码准备好的时候
        @Override
        public void onPrepared(MediaPlayer mp) {
//            Toast.makeText(SystemVideoPlayer.this, "开始播放", Toast.LENGTH_SHORT).show();
            videoview.start();//开始播放
            //关联总长度
            duration = videoview.getDuration();
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
    private void showMediaController() {
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }

    //隐藏控制面板
    private void hideMediaController() {
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        isshowMediaController = false;
    }


    //播放出错的监听
    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            //1.播放网络视频的时候，网络中断 ---1.如果网络确实断了，可以提示网络断了；2.网络断断续续的,重新播放
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //获取手机所有连接管理对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();//获取NetworkInfo对象
            //NetworkInfo对象为空 则代表没有网络
            if (networkInfo == null) {
                for (int i = 0; i <= 5; i++) {
                    videoview.start();
                }
                Toast.makeText(SystemVideoPlayer.this, "当前网络不可以！", Toast.LENGTH_SHORT).show();
            } else {
                //2.播放的视频的格式不支持 ---跳转到万能播放器播放
                startVitamioPlayer();
            }
            //播放出错跳出对话框
            showErrorDialog();
            //3.播放的时候本地文件中间有空白 ---下载做完成
            return true;
        }
    }

    //跳转到万能播放器
    /*
    1.把数据传入VitamVigeoPlayer播放器
    2.关闭系统播放器
     */
    private void startVitamioPlayer() {

        if (videoview != null) {
            videoview.stopPlayback();
        }
        Intent intent = new Intent(this, VitamioVideoPlayer.class);
        if (mediaItems != null && mediaItems.size() > 0) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);

        } else if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
        finish();//关闭页面
    }

    //播放出错跳出对话框
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当你播放一个视频黑屏或者花屏时！你要尝试使用万能播放器播放吗！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //2.播放的视频的格式不支持 ---跳转到万能播放器播放
                startVitamioPlayer();
                finish();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    //播放完成的监听(根据播放模式设置播放完成后的操作)
    String playmode ="EPEAT_NORMAL";//设置播放器的播放模式默认是顺序播放

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            switch (playmode) {
                case "EPEAT_NORMAL"://播放模式的标记，顺序播放
                    playNextVideo();
                    break;
                case "REPEAT_SINGLE"://播放模式的标记，单集循环播放
                    videoview.resume();//重新播放
                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                    break;
                case "REPEAT_SUSPEND":
                    videoview.pause();//播放完成后暂停
                    bntVideoStartPause.setBackgroundResource(R.drawable.bnt_video_start_selector);
                    break;
                case "REPEAT_ALL"://播放模式的标记，列表循环播放
                    Toast.makeText(SystemVideoPlayer.this, "列表循环播放"+position, Toast.LENGTH_SHORT).show();
                    if (mediaItems != null && mediaItems.size() == position+1) {
                        //播放下一个
                        position=0;
                        if (position < mediaItems.size()) {
                            MediaItem mediaItem = mediaItems.get(position);
                            tvName.setText(mediaItem.getName());
                            isNetUri = utils.isNetUri(mediaItem.getData());
                            videoview.setVideoPath(mediaItem.getData());
                            //设置按钮状态
                            bntVideoPre.setBackgroundResource(R.drawable.bnt_video_pre_selector);
                            bntVideoPre.setEnabled(true);
                            bntVideoNext.setBackgroundResource(R.drawable.bnt_video_next_selector);
                            bntVideoNext.setEnabled(true);
                            bntVideoStartPause.setBackgroundResource(R.drawable.bnt_start_pause_selector);
                            ll_loading.setVisibility(View.VISIBLE);
                        }
                    }else {
                        playNextVideo();
                    }
                    break;
            }
        }
    }

    //网络视频播放卡顿的监听(系统自带的方法)，也可以通过（当前播放进度 - 上一次播放进度 < 0 ,就卡)
    class MyOnInfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频卡顿，拖动卡
//                    Toast.makeText(SystemVideoPlayer.this, "视频正在加载中....!", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://视频卡顿结束，拖动卡结束
//                    Toast.makeText(SystemVideoPlayer.this, "视频加载完成!", Toast.LENGTH_SHORT).show();
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }


/*-----------------------------------------------------监听事件结束--------------------------------------------------------------------------------------*/


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

        //判断当前的屏幕状态是横屏还是竖屏
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int orientation = mConfiguration.orientation; //获取屏幕方向
        if (orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏时

            //3.把事件传递个手势识别器
            detector.onTouchEvent(event);
            //判断触摸屏幕的方式
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://手指按下
                    //1.按下时记录值
                    startY = event.getY();
                    startX = event.getX();
                    mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    tochRang = Math.min(screenHeight, screenWidth);
                    handler.removeMessages(2);
                    break;
                case MotionEvent.ACTION_MOVE://手指移动
                    //2.移动时记录值
                    float endY = event.getY();//移动时Y的值（会变化）
                    float endX = event.getX();//移动时X的值（会变化）
                    float distanceY = startY - endY;//Y移动的距离（上取正，下取负）
                    float distanceX = endX - startX;//X移动的距离（右取正，左取负）

                    //触摸面积小于屏幕宽度的一半的位置（左边屏幕）
                    if (endX < screenWidth / 2) {
                        //向上滑动时的操作
                    /*
                        Math.abs(x)=|x|；如果参数是非负数，则返回该参数。如果参数是负数，则返回该参数的相反数。
                     */
                        if (distanceY > 0 && (Math.abs(distanceY) > 200) && endY > 400) { //向上滑动（distanceY > 0），并且滑动的距离大于200，并且在距离屏幕大于400xp的地方滑动时执行下面的方法
                            Toast.makeText(this, "向上滑动", Toast.LENGTH_SHORT).show();
                            Log.v("向上滑动", String.valueOf(distanceY));
                            LL_Brightness.setVisibility(View.VISIBLE);
                            LL_Volume.setVisibility(View.GONE);
                            //改变亮度 = （滑动屏幕的距离： 总距离）*亮度最大值
                            float delta1 = ((distanceY / tochRang)) / 15 * 255;
                            //最终亮度 = 原来的 + 改变亮度；
                            FinalBrightness = (int) Math.min(Math.max(screenBrightness + delta1, 0), 255);
                            if (delta1 != 0) {
                                setScreenBrightness(FinalBrightness);
                            }
                            seekbar_Light.setProgress(FinalBrightness);
                            screenBrightness = FinalBrightness;
                        } else //向下滑动时的操作
                            if (distanceY < 0 && (Math.abs(distanceY) > 200) && endY > 400) {
                                Toast.makeText(this, "向下滑动", Toast.LENGTH_SHORT).show();
                                Log.v("向下滑动", String.valueOf(distanceY));
                                LL_Brightness.setVisibility(View.VISIBLE);
                                LL_Volume.setVisibility(View.GONE);
                                //改变亮度 = （滑动屏幕的距离： 总距离）*亮度最大值
                                float delta1 = ((distanceY / tochRang)) / 25 * 255;
                                //最终亮度 = 原来的 + 改变亮度；
                                FinalBrightness = (int) Math.min(Math.max(screenBrightness + delta1, 0), 255);
                                if (delta1 != 0) {
                                    setScreenBrightness(FinalBrightness);
                                }
                                seekbar_Light.setProgress(FinalBrightness);
                                screenBrightness = FinalBrightness;
                            }
                    } else { //触摸面积大于屏幕宽度的一半的位置（右边屏幕）
                        //向上滑动时的操作
                        if (distanceY > 0 && (Math.abs(distanceY) > 100) && endY > 400) {
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
                        } else //向下滑动时的操作
                            if (distanceY < 0 && (Math.abs(distanceY) > 100) && endY > 400) {
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
                                    updataVoice(FinalVolume, isMute);
                                }
                            }
                    }

                    //左右滑动改变视频的进度
                    if (distanceX > 0 && (Math.abs(distanceX) > 150)) {
                        Toast.makeText(this, "向右滑动", Toast.LENGTH_SHORT).show();
                        Log.v("向右滑动", String.valueOf(distanceX));
                        float delta2 = (distanceX / screenWidth) / 100 * duration;
                        int currentPosition = videoview.getCurrentPosition();
                        FinalProgress = (int) Math.min(Math.max(currentPosition + delta2, 0), duration);
                        videoview.seekTo(FinalProgress);


                    } else if (distanceX < 0 && (Math.abs(distanceX) > 150)) {
                        Toast.makeText(this, "向左滑动", Toast.LENGTH_SHORT).show();
                        Log.v("向左滑动", String.valueOf(distanceX));
                        float delta2 = (distanceX / screenWidth) / 100 * duration;
                        int currentPosition = videoview.getCurrentPosition();
                        FinalProgress = (int) Math.min(Math.max(currentPosition + delta2, 0), duration);
                        videoview.seekTo(FinalProgress);


                    }

                    break;

                case MotionEvent.ACTION_UP://手指离开
                    handler.sendEmptyMessageDelayed(3, 3000);//发消息隐藏LL_Volume和LL_Brightness
                    handler.sendEmptyMessageDelayed(2, 3000);//发消息隐藏控制面板
                    break;

            }


        } else {
            //竖屏
            //3.把事件传递个手势识别器
            detector.onTouchEvent(event);


        }
        return super.onTouchEvent(event);
    }



    //监听物理键实现声音大小的变化
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            LL_Volume.setVisibility(View.VISIBLE);
            currentVoice--;
            updataVoice(currentVoice, false);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.sendEmptyMessageDelayed(2, 3000);
            handler.sendEmptyMessageDelayed(3, 2000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            LL_Volume.setVisibility(View.VISIBLE);
            currentVoice++;
            updataVoice(currentVoice, false);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.sendEmptyMessageDelayed(2, 3000);
            handler.sendEmptyMessageDelayed(3, 2000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    /*---------------------------------------Activity的生命周期方法-------------------------------------------------------*/
    @Override
    //(activity被另一个透明或者Dialog样式的activity覆盖了）之后dialog取消，activity回到可交互状态，调用onResume()
    protected void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }


    @Override
    //Activity的生命周期的销毁方法
    protected void onDestroy() {
        //移除所有的消息
        handler.removeCallbacksAndMessages(null);
        //释放资源的时候要写在super的前面，先释放子类，再释放父类
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        LogUtil.e("onDestroy");
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    //当系统的配置信息发生改变时，系统会调用此方法(newConfig：新的设备配置信息)
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 在 View 或 Activity 处理横竖屏切换.的方法
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            Toast.makeText(this, "宽 = " + screenHeight + ",高 = " + screenWidth, Toast.LENGTH_SHORT).show();
            //设置视频的大小
//            videoview.setVideoSize(screenHeight, screenWidth);
            ll_Content.setVisibility(View.GONE);
            bntLike.setVisibility(View.VISIBLE);
            bntNoLike.setVisibility(View.VISIBLE);
            bntInsertCoins.setVisibility(View.VISIBLE);
            bntShare.setVisibility(View.VISIBLE);
            bntMenus.setVisibility(View.VISIBLE);
            bnt_addDanmaku.setVisibility(View.VISIBLE);
            bntVideoSiwchScreen.setVisibility(View.VISIBLE);
            bntDanmakuSet.setVisibility(View.VISIBLE);
            bntDanmakuHideAndShow.setVisibility(View.VISIBLE);
            ll_Content.setVisibility(View.GONE);

            mDanmakuView.getConfig().setDanmakuMargin(40);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            Toast.makeText(this, "宽= " + screenWidth + ",高 = " + screenHeight, Toast.LENGTH_SHORT).show();
            //设置视频的大小
//            videoview.setVideoSize(screenHeight, screenWidth);
            ll_Content.setVisibility(View.VISIBLE);
            bntLike.setVisibility(View.GONE);
            bntNoLike.setVisibility(View.GONE);
            bntInsertCoins.setVisibility(View.GONE);
            bntShare.setVisibility(View.GONE);
            bntMenus.setVisibility(View.GONE);
            bnt_addDanmaku.setVisibility(View.GONE);
            ll_Content.setVisibility(View.GONE);
            bntDanmakuSet.setVisibility(View.GONE);


            mDanmakuView.getConfig().setDanmakuMargin(20);
        }


    }

    @Override
    //检测到用户的按的键
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }



}
