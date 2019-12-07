package com.example.textthread.HttpUtils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {
    //处理json数据或者Img
    public static void handleOkhttp(String url,String method,Callback callback)
    {
        //请求的方式
        OkHttpClient client=new OkHttpClient();//定义Okhttp的客户端
        if (method=="get")
        {
            Request request =new Request.Builder()//创建请求对象;里面的（常用）方法有：url()是指定访问的地址（接口），
                    // get()和post()是代表访问接口的方式默认是get()方式；head（），header（），headers（），addHeader（）表示接口的头部消息（用来做身份的验真）
                    //最后要调用build（）才能才生Request对象
                    .url(url)
                    .get()
                    .build();
            //用异步访问方式
            client.newCall(request).enqueue(callback);
        }

        if (method=="post")
        {
            RequestBody body = null;
            Request request =new Request.Builder()//创建请求对象;里面的（常用）方法有：url()是指定访问的地址（接口），
                    // get()和post()是代表访问接口的方式默认是get()方式；head（），header（），headers（），addHeader（）表示接口的头部消息（用来做身份的验真）
                    //最后要调用build（）才能才生Request对象
                    .url(url)
                    .post(body)
                    .build();
            //用异步访问方式
            client.newCall(request).enqueue(callback);
        }
    }
}
