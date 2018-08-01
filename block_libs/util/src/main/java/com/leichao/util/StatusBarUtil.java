package com.leichao.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

    /**
     * 设置Activity的状态栏颜色
     *
     * @param activity Activity
     * @param color    颜色值
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度，单位px
     */
    public static int getStatusBarHeight() {
        Context context = AppUtil.getApp();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏文字颜色
     *
     * @param activity Activity
     * @param darkmode 状态栏文字颜色是否为黑色
     */
    public static boolean setStatusBarTextDark(Activity activity, boolean darkmode) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上版本
            if (darkmode) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            result = true;
        } else if (FixStatusBarColor.setMeiZuStatusBarDark(activity, darkmode)) {
            result = true;
        } else if (FixStatusBarColor.setXiaoMiStatusBarDark(activity, darkmode)) {
            result = true;
        }
        return result;
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class FixStatusBarColor {
        /**
         * 是否将魅族手机状态栏文字颜色更改为黑色，true为黑色，false为白色
         */
        private static boolean setMeiZuStatusBarDark(Activity activity, boolean darkmode) {
            boolean result = false;
            try {
                Window window = activity.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkmode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                LogUtil.d("MeiZu setStatusBarDark failed");
            }
            return result;
        }

        /**
         * 是否将小米手机状态栏文字颜色更改为黑色，true为黑色，false为白色
         */
        private static boolean setXiaoMiStatusBarDark(Activity activity, boolean darkmode) {
            boolean result = false;
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
                result = true;
            } catch (Exception e) {
                LogUtil.d("XiaoMi setStatusBarDark failed");
            }
            return result;
        }
    }

}
