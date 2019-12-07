package com.example.textthread.TabLayout.view;


import android.content.Context;
import android.media.PlaybackParams;
import android.util.AttributeSet;
import android.view.ViewGroup;

//自定义VideoView
public class VideoView extends android.widget.VideoView {
    int videoWidth;
    int videoHeight;
    //在代码中创建的时候一般用这个方法
    public VideoView(Context context) {
        this(context,null);
    }

    //当这个类在布局文件的时候，系统通过构造方法实例化该类（一定要用）
    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    //当需要设置样式的时候调用该方法
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //测量VideoView的宽和高
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    //在OnMeasure的时候，就对这个长宽比进行了处理,把处理的代码屏蔽掉，视频大小就可以随着VideoView的长宽改变而改变
//        int width = getDefaultSize(videoWidth, widthMeasureSpec);
//        int height = getDefaultSize(videoHeight, heightMeasureSpec);
//        if ( videoWidth * height  > width * videoHeight ) {
//            height = width * videoHeight / videoWidth;
//        }else if ( videoWidth * height  < width * videoHeight ) {
//            width = height * videoWidth / videoHeight;
//        }
//        setMeasuredDimension(width,height);
//    }

    //设置视频的宽和高
    /*
    int videoWidth:指定视频的宽
    int videoHeight:指定视频的高
     */
    public void setVideoSize(int videoWidth,int videoHeight){
        ViewGroup.LayoutParams params = getLayoutParams();
        this.videoWidth=videoWidth;
        this.videoHeight=videoHeight;
        params.width = videoWidth;
        params.height = videoHeight;
        setLayoutParams(params);
    }



}
