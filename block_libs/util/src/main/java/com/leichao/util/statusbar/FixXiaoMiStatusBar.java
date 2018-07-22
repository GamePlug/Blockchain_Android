package com.leichao.util.statusbar;

import android.app.Activity;
import android.view.Window;

import com.leichao.util.log.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FixXiaoMiStatusBar {

    /**
     * 是否将小米手机状态栏文字颜色更改为黑色，true为黑色，false为白色
     */
    static boolean setStatusBarDark(Activity activity, boolean darkmode) {
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
