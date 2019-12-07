package com.example.textthread;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class webViewActivity extends Activity {
    Button bntBiliBili,bntAGE,bntDiliDili;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        bntBiliBili=findViewById(R.id.bntBiliBili);
        bntAGE=findViewById(R.id.bntAGE);
        bntDiliDili=findViewById(R.id.bntDiliDili);
        webView=findViewById(R.id.webView);

        bntBiliBili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不调用浏览器，相当于自定义一个浏览器
                webView.setWebViewClient(new WebViewClient());
                //设置支持JavaScript
                webView.getSettings().setJavaScriptEnabled(true);
                //加载网络的网页，也可以加载本地的网页
                webView.loadUrl("https://www.baidu.com/");
            }
        });

        bntAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不调用浏览器，相当于自定义一个浏览器
                webView.setWebViewClient(new WebViewClient());
                //设置支持JavaScript
                webView.getSettings().setJavaScriptEnabled(true);
                //加载网络的网页，也可以加载本地的网页
                webView.loadUrl("http://m.imomoe.jp/");
            }
        });

        bntDiliDili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不调用浏览器，相当于自定义一个浏览器
                webView.setWebViewClient(new WebViewClient());
                //设置支持JavaScript(设置支持javaScript脚步语言)
                webView.getSettings().setJavaScriptEnabled(true);
                ////设置支持js调用java
                webView.addJavascriptInterface(new AndroidAndJSInterface(),"android");//必须要和HTML里的Js相对应 "android"
                //加载网络的网页，也可以加载本地的网页
                webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");//加载本地网页
            }
        });

    }

    class AndroidAndJSInterface {
        /**
         * 该方法将被js调用
         * @param id
         * @param videoUrl
         * @param tile
         */
        @JavascriptInterface//API12以是要写不然会（addJavascriptInterface）会报错
        //必须要和HTML里的Js的方法名相对应
        public void playVideo(int id,String videoUrl,String tile){
            //调起系统所有播放器,并且播放
            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(videoUrl),"video/*");
            startActivity(intent);
            //弹出网址链接
            Toast.makeText(webViewActivity.this, "网址:"+videoUrl, Toast.LENGTH_SHORT).show();
        }
    }

}
