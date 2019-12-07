package com.example.textthread.permission.request;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：CheckPermission 检测权限
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class CheckPermission {
    /**
     * 检查是否拥有指定的所有权限
     * @param context 上下文
     * @param permissions 权限数组
     * @return 只要有一个权限没有被授予, 则直接返回 false，否则，返回true!
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查未允许的权限集合
     * @param context 上下文
     * @param permissions 权限集合
     * @return 未允许的权限集合
     */
    public static List<String> checkPermissionDenied(Context context, String[] permissions){
        List<String> lstPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                lstPermissions.add(permission);
            }
        }
        return lstPermissions;
    }
}
