package com.example.textthread.JsonAsyncTask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.textthread.JsonAsyncTachModel.Weather;
import com.example.textthread.JsonAsyncTask.Utils.HttpJsonUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class JsonAsyncTask extends AsyncTask<String,Integer,String> {
    TextView tvResult,tv;//定义变量
    //写一个构造方法，并把参数传过来
    public JsonAsyncTask(TextView tvResult,TextView tv) {
        this.tvResult=tvResult;//把传过来的参数和类里的变量做挂接
        this.tv=tv;//把传过来的参数和类里的变量做挂接
    }
    @Override
    protected String doInBackground(String... params)
    {

        /*
        StringBuilder builder = new StringBuilder();//定义一个可变的字符序列（StringBuilder 是一个可变的字符序列）
        String url = params[0];//把execute(url)传过来的数据，通过params[0]（数组下标从0开始（0：代表第一个数据））数组赋值给url
        try{//做异常处理
            URL myUrl = new URL(url);//定义URL对象，并把url赋值个myUrl
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();//打开链接（url），返回HttpURLConnection对象赋值给connection
          //InputStreamReader类是从字节流到字符流的桥接器：它使用指定的字符集读取字节并将它们解码为字符。 它使用的字符集可以通过名称指定，也可以明确指定，或者可以接受平台的默认字符集。每次调用一个InputStreamReader的read（）方法都可能导致从底层字节输入流中读取一个或多个字节。 为了实现字节到字符的有效转换，可以从基础流中提取比满足当前读取操作所需的更多字节。为了获得最高效率，请考虑在BufferedReader中包装InputStreamReader
            InputStreamReader isReader = new InputStreamReader(connection.getInputStream());//connection.getInputStream():定义输入流，并赋值给isReader
            BufferedReader buffReader = new BufferedReader(isReader);//定义BufferedReader变量,为其他字符输入流添加一些缓冲功能
            String line = "";//定义字符串line，让他每次读取一行数据
            while((line = buffReader.readLine())!=null)//判断读取文件是否到文件尾，如果不是在读下一行，直到文件尾；readLine()：读取一行数据
            {
                builder.append(line);//把一行的数据写入可变的字符序列builder里；append():追加
            }
        }catch (Exception e)
        {
            Log.v("task",e.getMessage());//打印日志
        }
        return builder.toString();//返回一个含有builder数据的字符串
        */
        //通过调用外部类里的静态方法处理Json数据
        return HttpJsonUtils.getJsonData( params[0]);//返回一个含有builder数据的字符串
    }
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if(s!=null){//判断doInBackground传回来的数据是否为空
            Gson gson = new Gson();//建一个Gson对象
            try {
                //要指明转换的类型
                Weather weather = gson.fromJson(s,Weather.class);//fromJson（）是把Json字符串转换为其他类型（类型，对象，模型）（一定要保证Json的格式是正确的），T：代表泛型；toJson()是把其他类型转换为Json字符串
                String result = weather.getWeatherinfo().getCity() + "   " + weather.getWeatherinfo().getTemp();//获取转换后的单个数据，并赋值给result
                tvResult.setText(result);//在tvResult中显示出来
            }catch (Exception e)
            {
                Log.v("Gson异常",e.getMessage());
            }
        }
        tv.setText(s);//在tv中显示出来

    }
}
