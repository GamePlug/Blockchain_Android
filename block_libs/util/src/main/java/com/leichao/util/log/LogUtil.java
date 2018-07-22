package com.leichao.util.log;

import android.util.Log;

public class LogUtil {

    public static String TAG = "LogUtil";
    public static boolean isDebug = true;

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

}
