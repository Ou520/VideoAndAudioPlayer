package com.example.textthread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class StartupPage1Activity extends Activity {
    Button bntStart,bntStop,bntLoad,bntLoad1;
    TextView tvLoad;
    int index=1;
    boolean loop=true;
    Handler handler;
    ProgressBar pbLoad;
    @SuppressLint({"HandlerLeak", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_page1);
        bntStart=findViewById(R.id.bntStart);
        bntStop=findViewById(R.id.bntStop);
        bntLoad=findViewById(R.id.bntLoad);
        tvLoad=findViewById(R.id.tvLoad);
        pbLoad=findViewById(R.id.pbLoad);
        bntLoad1=findViewById(R.id.bntLoad1);

        //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==101)
                {
                    int index=msg.arg1;
                    //设置消息提示
                    Toast toast=Toast.makeText(StartupPage1Activity.this,String.valueOf(index) , Toast.LENGTH_SHORT);

                    toast.setGravity(Gravity.CENTER,0,0); //设置Toast显示内容居中
                    LinearLayout toast_layout=(LinearLayout) toast.getView();//将Toast作为一个布局
                    ImageView image=new ImageView(StartupPage1Activity.this);//定义ImageView变量，并对它初始化
                    image.setBackgroundColor(Color.GRAY);
                    image.setImageResource(R.mipmap.ic_launcher_round);//设置图片
                    TextView messageTextView = (TextView) toast_layout.getChildAt(0);//定义TextView变量，并对它初始化
                    messageTextView.setBackgroundColor(Color.GRAY);
                    messageTextView.setTextSize(40);//  修改Toast消息提示框字体大小
                    toast_layout.addView(image,0);//将图片添加到Toast上显示，0代表位置
                    showMyToast(toast, 800);//单位是毫秒,实现Toast显示时间的自定义
                }
                if (msg.what ==102)
                {
                    tvLoad.setText(msg.arg1+" %");
                    tvLoad.setVisibility(View.VISIBLE);//使隐藏的TextView显示出来
                    pbLoad.setVisibility(View.VISIBLE);//使隐藏的ProgressBar显示出来
                    pbLoad.setProgress(msg.arg1);//设置Progress的值为msg.arg1
                    if (msg.arg1 ==100)//当Progress的值为100时给出提示
                    {
                        Toast.makeText(StartupPage1Activity.this, "加载完成（子线程）！！", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        };

        //给bntStart按钮写监听,并创建线程
        bntStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      //--------------------------------方法2开始--------------------------------------------------
//                new Thread(new MyThread()).start();
      //--------------------------------方法2结束--------------------------------------------------

      //--------------------------------方法3开始--------------------------------------------------
                    new MyThread().start();
      //--------------------------------方法3结束--------------------------------------------------


      //--------------------------------方法1开始--------------------------------------------------
                /*
                //创建子线程，创建Runnable()接口
                new Thread(new Runnable() {
                    @Override
                    //实现Runnable接口，并重写run()方法，子线程要实现的功能写在run()方法里
                    public void run() {
                       while (loop)
                       {
                  //使用休眠sleep（）方法时会出现InterruptedException异常，要进行异常处理
                           try {
                               Thread.sleep(1000);//休眠1000毫秒，1000毫秒=1秒
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           Log.v("thread:",index+"");
                           index++;
                       }
                    }
                }
                ).start();//start()是启动线程
                */
                //--------------------------------方法1结束--------------------------------------------------

            }
        });
        ////给bntStop按钮写监听，并使打印停止，线程并没有停止
        bntStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loop=false;
            }
        });

        //给bntLoad按钮写监听，使ProgressBar里的progress值发生改变
        bntLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现进度条的加载变化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int progess=0;
                        while (true)
                        {
                            int value =(int)(Math.random()*10);//定义整型变量value并给他赋值一个随机数；Math.random()是：产生0-1的随机数
                            progess +=value;//value累加完赋值给progess
                            if (progess >=100)//限制progess的值只能是100之内，超过100还是让信息发送回去100
                            {
                                progess=100;
                                Message msg=new Message();
                                msg.what=102;
                                msg.arg1=progess;
                                handler.sendMessage(msg);
                                break;//跳出循环
                            }
                            Message msg=new Message();
                            msg.what=102;
                            msg.arg1=progess;
                            handler.sendMessage(msg);
                            SystemClock.sleep(1000);//安卓的工具类，里的休眠方法让他休眠（好处是不用做异常处理）
                        }
                    }
                }).start();
            }
        });

     //给bntLoad1按钮写监听，使ProgressBar里的progress值发生改变
        bntLoad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProgressAsyncTask().execute();//String...表示可变变量--参数的个数是不确定的；execute()表示立即执行
            }
        });
    }
//bntLoad按钮有的类，并继承AsyncTask（异步任务类），其中Params：表示参数；Progress：表示进度（一般在进度条上显示）；Result：表示结果；
// 三个参数都要注意数据的类型问题，类型要根据实际情况来定（类型一定要用那个类型对应的的基本类型的类）
//String...表示可变变量--参数的个数是不确定的；
    class ProgressAsyncTask extends AsyncTask<String,Integer,String>
    {
        @Override
        //第一步执行onPreExecute(表示在什么之前执行)，做用是：做初始化工作
        protected void onPreExecute() {
            tvLoad.setVisibility(View.VISIBLE);//使隐藏的TextView显示出来
            pbLoad.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        //第二步doInBackground（表示在后台运行），注意参数问题
        //耗时间的工作在这里做；
        /* 参数的类型和AsyncTask<Params(的类型相当应)，，>也和execute()的参数类型相对应，
           在execute()里设置参数doInBackground就可以获取对应的参数；*/
        protected String doInBackground(String... strings)
        {
            int progess=0;
            while (true)
            {
                int value = (int) (Math.random() * 10);//定义整型变量value并给他赋值一个随机数；Math.random()是：产生0-1的随机数
                progess += value;//value累加完赋值给progess
                if (progess >= 100)//限制progess的值只能是100之内，超过100还是让信息发送回去100
                {
                    publishProgress(100);
                    break;//跳出循环
                }
                publishProgress(progess);//用发布信息来触发进度变化进而执行onProgressUpdate方法，并可以给onProgressUpdate传递参数，参数的类型和onProgressUpdate的参数类型有关
                SystemClock.sleep(1000);//休眠1000毫秒，再发送参数
            }
            return "success";//返回一个结果给onPostExecute，所以结果类型要和onPostExecute的参数类型一致
        }
        @Override
        //第三步如果有进度变化（doInBackground触发），执行onProgressUpdate（表示进度不断在更新时执行），要注意参数问题
        //参数的类型和由AsyncTask<,Progress(的类型相当应)，>决定
        protected void onProgressUpdate(Integer... values) {//values是个数组
            super.onProgressUpdate(values);
            if (values!=null)
            {
                tvLoad.setText(values[0]+" %");
                pbLoad.setProgress(values[0]);
            }
        }
        @Override
        //第四步onCancelled（表示在做取消操作时会有什么变化时执行的）
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
        @Override
        //第五步doInBackground执行结束之后再执行onPostExecute（表示在什么之后执行），注意参数问题
        //参数的类型和由AsyncTask<，，Result(的类型相当应)>决定
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //呈现结果写在这里
            if (s!=null && s.equals("success"))//判断doInBackground传过来的值是否一致
            {
                Toast.makeText(StartupPage1Activity.this, "加载成功（异步）！", Toast.LENGTH_SHORT).show();
            }
        }

    }



//----------------------方法2----------------------------------------------------
    //写一个类继承Runnable接口
    /*
    class MyThread implements Runnable
    {
        //实现Runnable接口，并重写里面的run()方法
        @Override
        public void run()
        {
            while (loop)
            {
                //使用休眠sleep（）方法时会出现InterruptedException异常，要进行异常处理
                try {
                    Thread.sleep(1000);//休眠1000毫秒，1000毫秒=1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("thread", index + "");
                index++;
            }
        }
    }
    */
//------------------------------------------------------------------------------

//----------------------方法3----------------------------------------------------
    //重写一个线程类，要继承Thread（线程类的基类）并继承Runnable接口
    //可以把他拿出来，当公共类
    class MyThread extends Thread implements Runnable
    {
        //实现Runnable接口，并重写里面的run()方法
        @Override
        public void run()
        {
            while (loop)
            {
                Message msg=new Message();
                msg.what=101;
                msg.arg1=index;
                handler.sendMessage(msg);
                SystemClock.sleep(1000);//安卓的工具类，里的休眠方法让他休眠（好处是不用做异常处理）
                /*
                //使用休眠sleep（）方法时会出现InterruptedException异常，要进行异常处理
                try {
                    Thread.sleep(1000);//休眠1000毫秒，1000毫秒=1秒//线程Thread类里的休眠方法，要做异常处理
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                index++;
            }
        }
    }
//-------------------------------------------------------------------------------

    //-------------------写一个方法实现对Toast时间的自定义----------------------
    public void showMyToast(final Toast toast,final int cnt)
    {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        },cnt);
    }
    //-------------------------------------------------------------------

    public void bntReturnOoClick(View v)
    {
        switch (v.getId())
        {
         case R.id.bntReturn:
                startActivityForResult(new Intent(StartupPage1Activity.this, MainActivity.class), 1);
                finish();
                onDestroy();
            case R.id.bntExit:
                finish();
                onDestroy();
        }
    }
    public void bntImg(View view)
    {
        startActivity(new Intent(StartupPage1Activity.this,ImgAsyncTaskActivity.class));
                finish();
    }
}
