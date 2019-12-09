package com.example.textthread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.HttpUtils.HttpUtils;
import com.example.textthread.ImdAsynTaak.NetworkUtil;
import com.example.textthread.ImdAsynTaak.SavaImage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpActivity extends Activity {
    TextView tvOkHttp;
    private Callback callback;
//    Handler handler;
    //(要不域名拿出去，再通过枚举引用过来)
    String url="http://api.m.mtime.cn/PageSubArea/TrailerList.api";//定义一个字符串存储网址

    //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
    @SuppressLint("HandlerLeak")
   private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==100)//通过msg.what判断消息是来自那个字线程的，并在UI线程的组件显示或者其他操作
            {
                String data=(String) msg.obj;
                tvOkHttp.setText(data);
            }
        }
    };



    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        tvOkHttp=findViewById(R.id.tvOkHttp);




        //方法二
        /*
        OkHttpClient client=new OkHttpClient();//定义Okhttp的客户端
        Request request =new Request.Builder()//创建请求对象;里面的（常用）方法有：url()是指定访问的地址（接口），
                // get()和post()是代表访问接口的方式默认是get()方式；head（），header（），headers（），addHeader（）表示接口的头部消息（用来做身份的验真）
                //最后要调用build（）才能才生Request对象
                .url(url)
                .get()
                .build();
        //用异步访问方式
        client.newCall(request).enqueue(new Callback() {//Callback()表示回调
            @Override
            //访问远程的数据失败后的操作
            public void onFailure(Call call, IOException e) {
                Log.v("fail",e.getMessage());
            }

            @Override
            //访问远程的数据成功后的操作
            public void onResponse(Call call, Response response) throws IOException {
                //还要做Json数据的转换（没做）

                Message msg=new Message();//创建Message对象
                //给对象指定参数
                msg.what=100;//标志（整型变量），用来判断消息是来自那个字线程
                msg.obj=response.body().string();
                handler.sendMessage(msg);
            }
        });
        */


        //OkHttp的回调
        callback =new Callback() {
            @Override
            //解析失败
            public void onFailure(Call call, IOException e) {
                Log.v("fail",e.getMessage());
            }

            @Override
            //解析成功
            public void onResponse(Call call, Response response) throws IOException {
                //还要做Json数据的转换（没做）
                String message=response.body().string();
                Message msg=new Message();//创建Message对象
                //给对象指定参数
                msg.what=100;//标志（整型变量），用来判断消息是来自那个字线程
                msg.obj=message;
                handler.sendMessage(msg);
            }
        };

        if (NetworkUtil.isReady(getApplicationContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
        {
             HttpUtils.handleOkhttp(url,"get",callback);
        }else
        {
            //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
            new AlertDialog.Builder(OkhttpActivity.this)
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




        //方法一
        /*
        new Thread(new Runnable() {//创建子线程
            @Override
            public void run() {

                OkHttpClient client=new OkHttpClient();//定义Okhttp的客户端
        Request request =new Request.Builder()//创建请求对象;里面的（常用）方法有：url()是指定访问的地址（接口），
                // get()和post()是代表访问接口的方式默认是get()方式；head（），header（），headers（），addHeader（）表示接口的头部消息（用来做身份的验真）
                //最后要调用build（）才能才生Request对象
                .url(url)
                .get()
                .build();
                try {
                    //用同步访问方式
                    Response response =client.newCall(request).execute();//给服务器发送请求;execute()表示同步访问方式,enqueue(Callback responseCallback)表示异步访问方式
                    Log.v("okhttp",response.body().string());
                } catch (IOException e) {
                    Log.v("okhttp",e.getMessage());
                }

            }
        }).start();
        */
    }
}
