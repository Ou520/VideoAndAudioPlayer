package com.example.textthread;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityActionBar extends AppCompatActivity  {//
    private Button bntContextMenu,bntPopupMenu,bntPopupWindow;
    private ImageView ivContextMenu;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        bntContextMenu = findViewById(R.id.bntContextMenu);
        bntPopupMenu = findViewById(R.id.bntPopupMenu);
        ivContextMenu = findViewById(R.id.ivContextMenu);
        bntPopupWindow = findViewById(R.id.bntPopupWindow);


        registerForContextMenu(bntContextMenu);//为控件(视图)注册上下文菜单
        registerForContextMenu(ivContextMenu);

        bntPopupMenu.setOnClickListener(new MybntOnClickListener());
        bntPopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoneEffect();
            }
        });
    }

    //无任何效果的弹窗
    private void showNoneEffect() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow = inflater.inflate(R.layout.popupwindow, null, false);//引入弹窗布局
        //绑定自定义样式布局里的控件
        Button bntPopupWindow1=vPopupWindow.findViewById(R.id.bntPopupWindow1);
        //为布局里的按钮设置监听（其他控件的操作类）
        bntPopupWindow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityActionBar.this, "你点击的按钮", Toast.LENGTH_SHORT).show();
            }
        });

        //定义PopupWindow视图(设置popupWindow的大小)
        //PopupWindow(View contentView, int width, int height, boolean focusable)
        //ActionBar.LayoutParams.WRAP_CONTENT,(根据内容来决定大小)
        //ActionBar.LayoutParams.MATCH_PARENT（为铺满）
         popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT , ActionBar.LayoutParams.WRAP_CONTENT, true);
        //设置背景透明
        addBackground();
        //设置进出动画(从底部向上进入，从上部向下退出)
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        //设置PopupWindow触摸事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下
//                        Toast.makeText(ActivityActionBar.this, "手指按下", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 手指移动
//                        Toast.makeText(ActivityActionBar.this, "手指移动", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 手指抬起
//                        Toast.makeText(ActivityActionBar.this, "手指抬起", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // 事件被拦截
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        // 超出区域
//                        Toast.makeText(ActivityActionBar.this, "超出区域", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        //关闭PopupWindow后的操作
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                leaveBackground();
                Toast.makeText(ActivityActionBar.this, "你离开了popupWindow", Toast.LENGTH_SHORT).show();
            }
        });


        //设置PopupWindow显示的位置（引入（父布局）依附的布局）
        View parentView = LayoutInflater.from(ActivityActionBar.this).inflate(R.layout.activity_action_bar, null);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM ,0, 0);//方法内有四个参数:
                                                                                            // partent：个人理解是要展现在哪个布局的上面,
                                                                                          // Gravity:TOP(页面顶部),BOTTOM(页面底部),CENTER(页面中心),LEFT(页面左侧中间),RIGHT(页面右侧中间);Gravity.LEFT|Gravity.BOTTOM，表示页面左下角
                                                                                         //x：x轴偏移量，正数向右偏移，负数向左偏移
                                                                                        //y：y轴偏移量，正数向下偏移，负数向上偏移

        int width = bntPopupWindow.getMeasuredWidth();//按钮宽度
        int p_width = popupWindow.getWidth();//popup宽度
        popupWindow.showAsDropDown(bntPopupWindow,(width/2)-(p_width/2),10);//表示在父控件的四周显示，位置要自己计算
       /*
        补充常用方法
        方法名	                               功能
        setOutsideTouchable(boolean)	        设置PopupWindow是否能响应外部点击事件
        setTouchable(boolean)	                设置PopupWindow是否能响应点击事件
        getWidth()	                            获取PopupWindow的宽度
        getHeight()	                            获取PopupWindow的高度
        dismiss()	                            PopupWindow消失
        setAnimationStyle(int animationStyle)	设置动画样式

         */
    }
    private void addBackground() {
        // 设置启动popupWindow背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;//调节透明度
        getWindow().setAttributes(lp);
    }
    private void leaveBackground() {
        // 设置离开popupWindow背景颜色为透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;//调节透明度
        getWindow().setAttributes(lp);
    }
/*--------------------------------------------------------------------------------------------------------------------------------------------*/
    class MybntOnClickListener implements View.OnClickListener{
        //点击按钮后，加载弹出式菜单
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {
            //创建弹出式菜单对象（最低版本11）
            PopupMenu popup = new PopupMenu(ActivityActionBar.this, v);//第二个参数是绑定的那个view
            //获取菜单填充器
            MenuInflater inflater = popup.getMenuInflater();
            //填充菜单
            inflater.inflate(R.menu.popupmenu, popup.getMenu());
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener(new MyOnMenuItemClickListener());
            //使用反射。强制显示菜单图标(没起作用)
            try {
                Field field = popup.getClass().getDeclaredField("mPopup");
                field.setAccessible(true);
                MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popup);
                mHelper.setForceShowIcon(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //显示PopupMenu
            popup.show(); //这一行代码不要忘记了

        }
    }

    class MyOnMenuItemClickListener implements  PopupMenu.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.copy:
                    Toast.makeText(ActivityActionBar.this, "复制", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delete:
                    Toast.makeText(ActivityActionBar.this, "删除···", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

/*--------------------------------------------------------------------------------------------------------------------------------------------*/
//创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
//上下文菜单的触发事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.start:
            Toast.makeText(this, "开始···", Toast.LENGTH_SHORT).show();
           break;
           case R.id.over:
                Toast.makeText(this, "结束···", Toast.LENGTH_SHORT).show();
            break;
            default:
             break;
         }
        return super.onContextItemSelected(item);
    }


/*--------------------------------------------------------------------------------------------------------------------------------------------*/
    @SuppressLint("RestrictedApi")
    @Override
       /*
        利用反射机制调用MenuBuilder的setOptionalIconsVisible方法设置mOptionalIconsVisible为true，给菜单设置图标时才可见
        让菜单同时显示图标和文字
     */
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add :
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
            case R.id.reset :
                Toast.makeText(this, "reset", Toast.LENGTH_SHORT).show();
            case R.id.about :
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();

             //子菜单的点击事件
            case R.id.setting1:
                Toast.makeText(this, "声音設置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting2:
                Toast.makeText(this, "背景設置", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
