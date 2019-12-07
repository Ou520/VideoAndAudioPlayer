package com.example.textthread.TitleBar.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.textthread.R;

//自定义标题栏
public class TitleBar extends LinearLayout implements View.OnClickListener {
    private View iv_icon;
    private View tv_search;
    private View rl_game;
    private View rl_download;
    private View rl_message;
    private View iv_search;
    private View iv_redact;
    private Context context;
    //代码中实例化该类的时候使用这个方法
    public TitleBar(Context context) {
        this(context,null);
    }
    //当布局文件使用该类的时候，安卓系统通过构造方法实例化该类（反射）
    public TitleBar(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }
    //当需要设置样式的时候，可以使用该方法
    public TitleBar(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    //当布局文件加载完成的时候回调这个方法

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //得到孩子的实例
        iv_icon=getChildAt(0);
        tv_search =getChildAt(1);
        rl_game =getChildAt(2);
        rl_download =getChildAt(3);
        rl_message =getChildAt(4);
        iv_search =getChildAt(5);
        iv_redact =getChildAt(6);
        //设置点击事件
        iv_icon.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        rl_download.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_redact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_icon:
                Toast.makeText(context, "图标", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_download:
                Toast.makeText(context, "下载", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_message:
                Toast.makeText(context, "消息", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_redact:
                Toast.makeText(context, "编辑", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
