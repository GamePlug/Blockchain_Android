package com.leichao.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppUtil {

    /**
     * 工具包初始化
     *
     * @param context Context
     */
    public static void init(Context context) {
        init((Application) context.getApplicationContext());
    }

    /**
     * 工具包初始化
     *
     * @param app Application
     */
    public static void init(Application app) {
        if (AppManager.app == null) {
            AppManager.app = app;
            AppManager.app.registerActivityLifecycleCallbacks(new AppStatus());
        }
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Application getApp() {
        AppManager.checkIsInit();
        return AppManager.app;
    }

    /**
     * App是否处于前台
     *
     * @return true应用在前台，false应用在后台
     */
    public static boolean isForeground() {
        return AppManager.isForeground;
    }

    /**
     * 获取最上层的Activity,若没有则返回null
     *
     * @return Activity
     */
    public static Activity getTopActivity() {
        return AppManager.getTopActivity();
    }

    /**
     * 添加App状态变化监听
     *
     * @param listener OnStatusListener
     */
    public static void addStatusListener(OnStatusListener listener) {
        if (!AppManager.listeners.contains(listener)) {
            AppManager.listeners.add(listener);
        }
    }

    /**
     * 移除App状态变化监听
     *
     * @param listener OnStatusListener
     */
    public static void removeStatusListener(OnStatusListener listener) {
        AppManager.listeners.remove(listener);
    }


    //------------------------------------------内部方法---------------------------------------------//

    // App处于前后台状态变化回调
    public interface OnStatusListener {
        void onForeground();

        void onBackground();
    }

    // App管理类
    static class AppManager {
        private static Application app;
        private static int activityCount = 0;
        private static boolean isForeground = true;// 应用是否在前台
        private static List<OnStatusListener> listeners = new ArrayList<>();
        private static LinkedList<Activity> activityList = new LinkedList<>();

        // 检查是否初始化
        static void checkIsInit() {
            if (app != null) {
                return;
            }
            try {
                @SuppressLint("PrivateApi")
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                Object at = activityThread.getMethod("currentActivityThread").invoke(null);
                Object app = activityThread.getMethod("getApplication").invoke(at);
                if (app != null) {
                    init((Application) app);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new NullPointerException("You should init AppUtil first on Application");
        }

        // 前台Activity数量增加
        static void upForeActivityNum() {
            activityCount++;
            if (activityCount >= 1 && !isForeground) {
                isForeground = true;
                for (OnStatusListener listener : listeners) {
                    listener.onForeground();
                }
            }
        }

        // 前台Activity数量减少
        static void downForeActivityNum() {
            activityCount--;
            if (activityCount <= 0 && isForeground) {
                isForeground = false;
                for (OnStatusListener listener : listeners) {
                    listener.onBackground();
                }
            }
        }

        // 添加Activity到队列
        static void addActivity(Activity activity) {
            if (activityList.contains(activity)) {
                if (!activityList.getLast().equals(activity)) {
                    activityList.remove(activity);
                    activityList.addLast(activity);
                }
            } else {
                activityList.addLast(activity);
            }
        }

        // 从队列移除Activity
        static void removeActivity(Activity activity) {
            activityList.remove(activity);
        }

        // 获取最上层的Activity
        static Activity getTopActivity() {
            if (!activityList.isEmpty()) {
                final Activity topActivity = activityList.getLast();
                if (topActivity != null) {
                    return topActivity;
                }
            }
            return null;
        }
    }

    // Activity生命周期回调
    static class AppStatus implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            AppManager.upForeActivityNum();
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            AppManager.downForeActivityNum();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.removeActivity(activity);
        }
    }

}
