package com.example.textthread.ImdAsynTaak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.util.function.LongToDoubleFunction;

public class SavaImage {
    Context context;//定义上下文对象
    int fileName;
    public SavaImage(Context context,int fileName) {

        this.context=context;
        this.fileName=fileName;
    }
    public void getSavaImge(Bitmap bitmap,String path)
    {
        if (bitmap!=null)
        {
            File file =new File(path);//通过将给定的路径名字字符串path转换为抽象路径名来创建一个新File实例
            FileOutputStream fileOutputStream=null;//定义文件输出流并赋值
            Log.v("文件的路径",path);
            //判断文件是否存在，不存在，则创建他
            if (!file.exists())
            {
                file.mkdir();
            }else
                {
                    Log.v("file","文件已经存在！");
//                    Toast.makeText(context, "文件已经存在！", Toast.LENGTH_SHORT).show();
                }
            try {
                //System.currentTimeMillis():获取系统时间给文件命名（可能会有问题（在同一时间内问））
                fileOutputStream =new FileOutputStream(path+"/"+fileName+".jpg");//创建一个向具有指定name的文件中写入数据的文件输出文件流
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);//压缩位图，要指定格式和图片质量和压缩的文件输出流
                fileOutputStream.flush();//刷新输出缓冲区
                fileOutputStream.close();//关闭输出流,关闭后的操作会产生IOException异常
            }catch (Exception e)
            {
                Log.v("file",e.getMessage());
            }

            Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
            //发广播通知系统相册刷新(没写！！)

        }else
            {
                Log.v("bitmap","bitmap为空！");
            }

    }
}
