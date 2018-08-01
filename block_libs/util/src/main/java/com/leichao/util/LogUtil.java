package com.leichao.util;

import android.util.Log;

public final class LogUtil {

    private LogUtil() {
    }

    /**
     * 设置是否开启打印日志
     *
     * @param debug true开启，false关闭
     */
    public static void setDebug(boolean debug) {
        Config.isDebug = debug;
    }

    /**
     * 设置日志打印的TAG
     *
     * @param tag TAG
     */
    public static void setTAG(String tag) {
        Config.TAG = tag;
    }

    /**
     * 打印debug类型日志
     *
     * @param msg 打印的信息
     */
    public static void d(String msg) {
        if (Config.isDebug) {
            Log.d(Config.TAG, msg);
        }
    }

    /**
     * 打印error类型日志
     *
     * @param msg 打印的信息
     */
    public static void e(String msg) {
        if (Config.isDebug) {
            Log.e(Config.TAG, msg);
        }
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class Config {
        private static String TAG = "LogUtil";
        private static boolean isDebug = true;
    }

}
