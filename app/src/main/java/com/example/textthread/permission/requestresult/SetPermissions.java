package com.example.textthread.permission.requestresult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.example.textthread.permission.PermissionUtils;


/**
 * 类：SetPermissions
 * 作者： qxc
 * 日期：2018/2/8.
 */

public class SetPermissions {
    /**
     * 打开APP详情页面，引导用户去设置权限
     * @param activity 页面对象
     * @param permissionNames 权限名称（如是多个，使用\n分割）
     */
    public static void openAppDetails(final Activity activity, String permissionNames) {
        StringBuilder sb = new StringBuilder();
        sb.append(PermissionUtils.PermissionTip1);
        sb.append(permissionNames);
        sb.append(PermissionUtils.PermissionTip2);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(sb.toString());
        builder.setPositiveButton(PermissionUtils.PermissionDialogPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton(PermissionUtils.PermissionDialogNegativeButton, null);
        builder.show();
    }
}
