package com.example.textthread.TitleBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.textthread.R;
import com.example.textthread.StartAllPlayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitlebarActivity extends AppCompatActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_titlebar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_icon)
    public void onViewClicked() {
        startActivity(new Intent(TitlebarActivity.this, StartAllPlayActivity.class));
    }
}
