package com.leichao.util;

import android.util.DisplayMetrics;

public class ScreenUtil {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 将dp转为px
     */
    public static int dip2px(float dpValue) {
        float scale = getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    /**
     * 将px转为dp
     */
    public static int px2dip(float pxValue) {
        float scale = getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static DisplayMetrics getDisplayMetrics() {
        return AppUtil.getApp().getResources().getDisplayMetrics();
    }

}
