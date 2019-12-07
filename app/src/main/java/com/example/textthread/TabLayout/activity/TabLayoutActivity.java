package com.example.textthread.TabLayout.activity;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.example.textthread.R;
import com.example.textthread.TabLayout.view.NoScrollViewPager;
import com.example.textthread.TabLayout.adapter.ViewPagerAdapter;

public class TabLayoutActivity extends AppCompatActivity {
    NoScrollViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
         viewPager=findViewById(R.id.viewPager);
         tabLayout=findViewById(R.id.tabLayout);

        //初始化数据
        //设置ViewPager适配器
        adapter =new ViewPagerAdapter(getSupportFragmentManager(),tabLayout);
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(1);//设置ViewPager默认显示第几个ViewPager
        //关联ViewPager
        tabLayout.setupWithViewPager(viewPager);
        //设置固定
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //TabLayout, ViewPager结合使用时给Tab添加图标
        tabLayout.getTabAt(0).setIcon(R.drawable.bofang2);
        tabLayout.getTabAt(1).setIcon(R.drawable.cahngpian);
        tabLayout.getTabAt(2).setIcon(R.drawable.bf3);
        tabLayout.getTabAt(3).setIcon(R.drawable.bf4);
    }



    private boolean isExit =false;
    @Override
    //判断是否真的想退出软件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (!isExit){
                isExit=true;
                Toast.makeText(this, "在两秒内在按一次就退出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit =false;
                    }
                },2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
