package com.leichao.util;

import android.view.View;

public final class ViewUtil {

    private ViewUtil() {
    }

    /**
     * 获取焦点
     */
    public static void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

}
