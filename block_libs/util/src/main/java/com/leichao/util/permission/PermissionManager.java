package com.leichao.util.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    private static volatile PermissionManager instance;
    private OnResultListener mListener;
    private String[] mPermissions;
    private ArrayList<String> mGrantedList = new ArrayList<>();// 有权限
    private ArrayList<String> mDeniedList = new ArrayList<>();// 无权限

    static PermissionManager getInstance() {
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }

    void request(Context context, String[] permissions, OnResultListener listener) {
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

    void result(Context context) {
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
            mListener.onResult(mGrantedList, mDeniedList);
        }
    }

    public interface OnResultListener {
        void onResult(List<String> grantedList, List<String> deniedList);
    }

}
