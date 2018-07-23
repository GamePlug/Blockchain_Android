package com.leichao.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.LinkedList;

public class AppUtil {

    /**
     * 工具包初始化
     *
     * @param context Context
     */
    public static void init(final Context context) {
        init((Application) context.getApplicationContext());
    }

    /**
     * 工具包初始化
     *
     * @param app Application
     */
    public static void init(final Application app) {
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
        if (AppManager.app != null) {
            return AppManager.app;
        }
        throw new NullPointerException("You should init AppUtil first");
    }

    /**
     * App是否处于后台
     *
     * @return true应用在后台，false应用在前台
     */
    public static boolean isBackground() {
        return AppManager.isBackground;
    }

    /**
     * 获取最上层的Activity,若没有则返回null
     *
     * @return Activity
     */
    public static Activity getTopActivity() {
        return AppManager.getTopActivity();
    }


    //------------------------------------------内部方法---------------------------------------------//

    static class AppManager {
        private static Application app;
        private static boolean isBackground = true;// 应用是否在后台
        private static LinkedList<Activity> activityList = new LinkedList<>();

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

        static void removeActivity(Activity activity) {
            activityList.remove(activity);
        }

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

    static class AppStatus implements Application.ActivityLifecycleCallbacks {

        private int mActivityCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivityCount++;
            AppManager.isBackground = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            mActivityCount--;
            if (mActivityCount == 0) {
                AppManager.isBackground = true;
            }
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
