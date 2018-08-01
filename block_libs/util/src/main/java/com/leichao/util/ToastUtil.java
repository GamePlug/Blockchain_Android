package com.leichao.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 显示Toast
 */
public final class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private ToastUtil() {
    }

    public static void show(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(AppUtil.getApp(), message, Toast.LENGTH_SHORT);
                    toast.show();
                    oneTime = System.currentTimeMillis();
                } else {
                    twoTime = System.currentTimeMillis();
                    if (message.equals(oldMsg)) {
                        if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                            toast.show();
                        }
                    } else {
                        oldMsg = message;
                        toast.setText(message);
                        toast.show();
                    }
                }
                oneTime = twoTime;
            }
        });
    }

    public static void show(int msg){
        show(AppUtil.getApp().getResources().getString(msg));
    }

}
