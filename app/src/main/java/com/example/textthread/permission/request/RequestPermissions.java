package com.example.textthread.permission.request;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.example.textthread.permission.request.IRequestPermissions;

import java.util.List;

/**
 * 类：RequestPermissions
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class RequestPermissions implements IRequestPermissions {
    private static RequestPermissions requestPermissions;
    public static RequestPermissions getInstance(){
        if(requestPermissions == null){
            requestPermissions = new RequestPermissions();
        }
        return requestPermissions;
    }

    @Override
    public boolean requestPermissions(Activity activity, String[] permissions, int resultCode) {
        //判断手机版本是否23以下，如果是，不需要使用动态权限
        if(Build.VERSION.SDK_INT < 23){
            return true;
        }

        //判断并请求权限
        return requestNeedPermission(activity,permissions,resultCode);
    }

    private boolean requestAllPermission(Activity activity, String[] permissions, int resultCode){
        //判断是否已赋予了全部权限
        boolean isAllGranted = CheckPermission.checkPermissionAllGranted(activity, permissions);
        if(isAllGranted){
            return true;
        }
        ActivityCompat.requestPermissions(activity, permissions, resultCode);
        return false;
    }

    private boolean requestNeedPermission(Activity activity, String[] permissions, int resultCode){
        List<String> list = CheckPermission.checkPermissionDenied(activity, permissions);
        if(list.size() == 0){
            return true;
        }

        //请求权限
        String[] deniedPermissions = list.toArray(new String[list.size()]);
        ActivityCompat.requestPermissions(activity, deniedPermissions, resultCode);
        return false;
    }
}
