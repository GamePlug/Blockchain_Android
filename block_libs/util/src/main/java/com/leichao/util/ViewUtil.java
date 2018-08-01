package com.leichao.util;

import android.view.View;

public class ViewUtil {

    /**
     * 获取焦点
     */
    public static void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

}
