package com.example.textthread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.textthread.ImdAsynTaak.Utils.HttpBitmapUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NotificationImagerShowActivity extends Activity {
    ImageView ivNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_imager_show);
        ivNotification=findViewById(R.id.ivNotification);
        //定义Intent变量，获取别的Activity传来的数据
        Intent myIntent=getIntent();
        //定义整型变量fileName并给他赋值Intent传来的数据
        int fileName= myIntent.getIntExtra("fileNameKey",0);

        Bitmap bitmap=getLoacalBitmap(Environment.getExternalStorageDirectory()+"/Pictures/"+fileName+".jpg");
        ivNotification.setImageBitmap(bitmap);
        }
        //写一个静态方法实现把SD卡里的图片转换为位图（Bitmap）
        public static Bitmap getLoacalBitmap(String url)
        {
            try {
                FileInputStream file =new FileInputStream(url);//定义文件输入流，把url路径里的文件转换为输入流
                return BitmapFactory.decodeStream(file);///把输入流通过工厂类（BitmapFactory.decodeStream）转化为Bitmap图片
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
}
