package com.leichao.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class StatusBarUtil {

    private StatusBarUtil() {
    }

    /**
     * 设置全屏沉浸式状态栏(沉浸式，即内容会延伸到状态栏)
     * 注：在界面中使用到输入框的时候，全屏沉浸式状态栏
     *    与输入法模式 {@link WindowManager.LayoutParams#SOFT_INPUT_ADJUST_RESIZE}有冲突，
     *    需要调用 {@link KeyboardUtil#fixAndroidBug5497(Activity)}用来解决冲突。
     *
     * @param activity Activity
     */
    public static void setFullTranslucent(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }

    /**
     * 设置状态栏背景颜色(非沉浸式，即内容不会延伸到状态栏)
     *
     * @param activity Activity
     * @param color    颜色值
     */
    public static void setBackgroundColor(Activity activity, int color) {
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
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 暂未兼容到19版本，若要兼容请参考：https://blog.csdn.net/u014418171/article/details/81223681
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度，单位px
     */
    public static int getHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * 设置状态栏文字颜色
     *
     * @param activity Activity
     * @param dark 状态栏文字颜色是否为黑色
     */
    public static boolean setTextColor(Activity activity, boolean dark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上版本
            View decorView = activity.getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (decorView.getSystemUiVisibility() != vis) {
                decorView.setSystemUiVisibility(vis);
            }
            result = true;
        } else if (FixStatusBarColor.setMeiZuColor(activity, dark)) {
            result = true;
        } else if (FixStatusBarColor.setXiaoMiColor(activity, dark)) {
            result = true;
        }
        return result;
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class FixStatusBarColor {
        /**
         * 是否将魅族手机状态栏文字颜色更改为黑色，true为黑色，false为白色
         */
        private static boolean setMeiZuColor(Activity activity, boolean dark) {
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
                if (dark) {
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
        private static boolean setXiaoMiColor(Activity activity, boolean dark) {
            boolean result = false;
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), dark ? darkModeFlag : 0, darkModeFlag);
                result = true;
            } catch (Exception e) {
                LogUtil.d("XiaoMi setStatusBarDark failed");
            }
            return result;
        }
    }

}
