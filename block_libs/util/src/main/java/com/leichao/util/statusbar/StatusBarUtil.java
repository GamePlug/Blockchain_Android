package com.leichao.util.statusbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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
     * @param context Context
     * @return 状态栏高度，单位px
     */
    public static int getStatusBarHeight(Context context) {
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
        } else if (FixMeiZuStatusBar.setStatusBarDark(activity, darkmode)) {
            result = true;
        } else if (FixXiaoMiStatusBar.setStatusBarDark(activity, darkmode)) {
            result = true;
        }
        return result;
    }

}
