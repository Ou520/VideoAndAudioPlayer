package com.example.textthread;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ButterKnifeActivity extends Activity {


    @BindView(R.id.tvButterKnife)
    TextView tvButterKnife;
    @BindView(R.id.bntButterKnife)
    Button bntButterKnife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.bntButterKnife)
    public void onViewClicked() {
        Toast.makeText(this, "你点击了按钮", Toast.LENGTH_SHORT).show();
    }
    @OnLongClick({R.id.bntButterKnife,R.id.tvButterKnife})
    public boolean onViewLongClick(){
        Toast.makeText(this, "你长按了按钮", Toast.LENGTH_SHORT).show();
        return true ;
    }

    @OnClick(R.id.tvButterKnife)
    public void onViewClicked1() {
        Toast.makeText(this, "你点击了文本框", Toast.LENGTH_SHORT).show();
    }
}
