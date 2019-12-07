package com.example.textthread.permission.request;

import android.app.Activity;

/**
 * 类：IRequestPermissions 申请权限
 * 作者： qxc
 * 日期：2018/2/8.
 */
public interface IRequestPermissions {
    /**
     * 请求权限
     * @param activity 上下文
     * @param permissions 权限集合
     * @param resultCode 请求码
     * @return 如果权限已全部允许，返回true; 反之，请求权限，在
     */
    boolean requestPermissions(Activity activity, String[] permissions, int resultCode);
}
