package com.leichao.util.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    /**
     * 请求权限
     *
     * @param context     Context
     * @param permissions Manifest.permission中的值
     * @param listener    权限结果回调监听
     */
    public static void request(Context context, String[] permissions, PermissionManager.OnResultListener listener) {
        PermissionManager.getInstance().request(context, permissions, listener);
    }

    /**
     * 检查是否有权限
     *
     * @param context    Context
     * @param permission Manifest.permission中的值
     * @return true有权限，false无权限
     */
    public static boolean check(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
