package com.leichao.util.statusbar;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.leichao.util.log.LogUtil;

import java.lang.reflect.Field;

public class FixMeiZuStatusBar {

    /**
     * 是否将魅族手机状态栏文字颜色更改为黑色，true为黑色，false为白色
     */
    static boolean setStatusBarDark(Activity activity, boolean darkmode) {
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

}
