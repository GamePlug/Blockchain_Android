package com.leichao.retrofit.util;

import android.util.Log;

import com.leichao.retrofit.HttpManager;

/**
 * 工具类
 * Created by leichao on 2017/7/22.
 */

public class LogUtil {

    private static final String TAG = "Retrofit";

    /**
     * 打印log
     */
    public static void logI(String str) {
        if (HttpManager.config().isDebug()) {
            Log.i(TAG, str);
        }
    }

    /**
     * 打印log
     */
    public static void logE(String str) {
        if (HttpManager.config().isDebug()) {
            Log.e(TAG, str);
        }
    }

}
