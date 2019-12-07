package com.example.textthread;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class BannerActivity extends AppCompatActivity implements OnBannerListener {
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
    }
    private void initView() {
        banner = (Banner) findViewById(R.id.banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3917100489,4080944761&fm=26&gp=0.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560954539327&di=c95341b9f01bd83f6541b75b7ad3b8f5&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-4o3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F37d3d539b6003af3de8986413c2ac65c1038b6e4.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560954369705&di=2048c2be749fdaba4a9c75592319a5ba&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F19a2a8d28a84324b726f84f1508851682cfb98ea.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560954394438&di=a758067a13fcafd9053ce7647597758d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201809%2F03%2F20180903153009_CG53K.jpeg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }
    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(this, "你点了第"+position+"张轮播图", Toast.LENGTH_SHORT).show();
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
