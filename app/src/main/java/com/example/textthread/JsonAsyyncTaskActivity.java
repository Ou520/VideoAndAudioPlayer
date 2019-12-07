package com.example.textthread;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.textthread.ImdAsynTaak.NetworkUtil;
import com.example.textthread.JsonAsyncTask.JsonAsyncTask;


public class JsonAsyyncTaskActivity extends Activity {
    TextView tvResult,tv;//定义变量
    Button bntOkHttp;
    String url="http://www.weather.com.cn/data/sk/101010100.html";//定义一个字符串存储网址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_asyync_task);
        tvResult=findViewById(R.id.tvResult);//做关联
        tv=findViewById(R.id.tv);//做关联
        bntOkHttp=findViewById(R.id.bntOkHttp);

        if (NetworkUtil.isReady(getApplicationContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
        {
                //异步任务
                 new JsonAsyncTask(tvResult,tv).execute(url);//通过匿名的方式引用JsonAsyncTask方法
        }else
        {
            //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
            new AlertDialog.Builder(JsonAsyyncTaskActivity.this)
                    .setTitle("当前网络不可用")
                    .setMessage("请设置网络！！！")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }


        bntOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(JsonAsyyncTaskActivity.this, OkhttpActivity.class), 1);
                finish();
            }
        });
    }
}

