package com.example.textthread.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

//通过引用布局文件来实现自定义对话框
public class DialogUtil {
    public static Dialog loadingDialog(Context context)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题（此属性也适用Activity ----requestWindowFeature(Window.FEATURE_NO_TITLE)也可以隐藏导航栏）
//        dialog.setContentView();//设置自定义对话框的样式，引用布局文件 也可以用Java代码来写一个视图（View）
        dialog.setCancelable(false);//点击别的位置是否可以取消显示对话框（false是不可以true是可以）
//        dialog.dismiss();//取消显示对话框
        return dialog;
    }
}
