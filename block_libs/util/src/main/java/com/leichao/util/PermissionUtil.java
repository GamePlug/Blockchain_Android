package com.leichao.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class PermissionUtil {

    private PermissionUtil() {
    }

    /**
     * 请求权限(单个)
     * 只有没有该permission权限时才去申请，否则直接回调结果
     *
     * @param permission Manifest.permission中的值 {@link android.Manifest.permission}
     * @param listener   权限结果回调监听
     */
    public static void request(String permission, OnPermissionResultListener listener) {
        request(new String[]{permission}, listener);
    }

    /**
     * 请求权限(多个)
     * 只有permissions中没有的权限时才去申请，如果所有权限都有则直接回调结果
     *
     * @param permissions Manifest.permission中的值 {@link android.Manifest.permission}
     * @param listener    权限结果回调监听
     */
    public static void request(String[] permissions, OnPermissionResultListener listener) {
        PermissionManager.getInstance().request(AppUtil.getApp(), permissions, listener);
    }

    /**
     * 检查是否有权限
     *
     * @param permission Manifest.permission中的值 {@link android.Manifest.permission}
     * @return true有权限，false无权限
     */
    public static boolean check(String permission) {
        return ContextCompat.checkSelfPermission(AppUtil.getApp(), permission) == PackageManager.PERMISSION_GRANTED;
    }


    //------------------------------------------内部方法---------------------------------------------//

    public interface OnPermissionResultListener {
        /**
         * 权限结果回调
         * @param grantedList 申请成功的权限
         * @param deniedList 申请失败的权限
         */
        void onPermissionResult(List<String> grantedList, List<String> deniedList);
    }

    private static class PermissionManager {

        private static volatile PermissionManager instance;
        private OnPermissionResultListener mListener;
        private String[] mPermissions;
        private ArrayList<String> mGrantedList = new ArrayList<>();// 有权限
        private ArrayList<String> mDeniedList = new ArrayList<>();// 无权限

        private static PermissionManager getInstance() {
            if (instance == null) {
                synchronized (PermissionManager.class) {
                    if (instance == null) {
                        instance = new PermissionManager();
                    }
                }
            }
            return instance;
        }

        private void request(Context context, String[] permissions, OnPermissionResultListener listener) {
            mPermissions = permissions;
            mListener = listener;
            checkPermissions(context);
            if (mDeniedList.size() > 0) {
                Intent intent = new Intent(context, PermissionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putStringArrayListExtra(PermissionActivity.PERMISSIONS, mDeniedList);
                context.startActivity(intent);
            } else {
                onListener();
            }
        }

        private void result(Context context) {
            checkPermissions(context);
            onListener();
        }

        private void checkPermissions(Context context) {
            mGrantedList.clear();
            mDeniedList.clear();
            for (String permission : mPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    mGrantedList.add(permission);
                } else {
                    mDeniedList.add(permission);
                }
            }
        }

        private void onListener() {
            if (mListener != null) {
                mListener.onPermissionResult(mGrantedList, mDeniedList);
                mListener = null;
            }
        }
    }

    public static class PermissionActivity extends Activity {

        private static final String PERMISSIONS = "permissions";
        private static final int REQUEST_CODE = 1000;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            fixTransparentThemeOrientation();
            super.onCreate(savedInstanceState);
            StatusBarUtil.setFullTranslucent(this);
            ArrayList<String> permissions = getIntent().getStringArrayListExtra(PERMISSIONS);
            if (permissions != null && permissions.size() > 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE);
            } else {
                result();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE) {
                result();
            }
        }

        private void result() {
            PermissionManager.getInstance().result(this);
            finish();
        }

        // Fix 8.0: Only fullscreen opaque activities can request orientation
        private void fixTransparentThemeOrientation() {
            Class clazz = getClass();
            while (clazz != null && !clazz.equals(Activity.class)) {
                clazz = clazz.getSuperclass();
                if (clazz.equals(Activity.class)) {
                    try {
                        Field field = clazz.getDeclaredField("mActivityInfo");
                        field.setAccessible(true);
                        ActivityInfo activityInfo = (ActivityInfo) field.get(this);
                        activityInfo.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
