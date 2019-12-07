package com.example.textthread.permission.requestresult;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 类：IRequestPermissionsResult
 * 作者： qxc
 * 日期：2018/2/8.
 */
public interface IRequestPermissionsResult {
    /**
     * 处理权限请求结果
     * @param activity
     * @param permissions 请求的权限数组
     * @param grantResults 权限请求结果数组
     * @return 处理权限结果如果全部通过，返回true；否则，引导用户去授权页面
     */
    boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults);
}
