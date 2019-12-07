package com.example.textthread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class GifShow extends Activity {

    @BindView(R.id.bnt_exit)
    Button bntExit;
    @BindView(R.id.iv_image_gif)
    GifImageView ivImageGif;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_show);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        url = intent.getStringExtra("GifurlKey");//图片地址
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivImageGif);
    }

    @OnClick(R.id.bnt_exit)
    public void onClick() {
        finish();
    }
}
