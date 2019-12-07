package com.example.textthread;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartAllPlayActivity extends Activity {

    @BindView(R.id.bnt_startAllPlay)
    Button bntStartAllPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_all_play);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bnt_startAllPlay)
    public void onViewClicked() {
        Intent intent =new Intent();
        intent.setDataAndType(Uri.parse("https://gss3.baidu.com/6LZ0ej3k1Qd3ote6lo7D0j9wehsv/tieba-smallvideo/607272_0e915dd9b3d7bcb03a4b7eb0c06a1f0a.mp4"),"video/*");
        startActivity(intent);
    }
}
