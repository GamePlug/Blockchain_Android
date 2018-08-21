package com.leichao.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class AppUtil {

    private AppUtil() {
    }

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
        AppManager.init(app);
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Application getApp() {
        return AppManager.getApp();
    }

    /**
     * App是否处于前台
     *
     * @return true应用在前台，false应用在后台
     */
    public static boolean isForeground() {
        return AppManager.isForeground();
    }

    /**
     * 添加App状态变化监听
     *
     * @param listener OnStatusListener
     */
    public static void addStatusListener(OnAppStatusListener listener) {
        AppManager.addStatusListener(listener);
    }

    /**
     * 移除App状态变化监听
     *
     * @param listener OnStatusListener
     */
    public static void removeStatusListener(OnAppStatusListener listener) {
        AppManager.removeStatusListener(listener);
    }

    /**
     * 获取Activity队列
     *
     * @return LinkedList<Activity>
     */
    public static LinkedList<Activity> getActivityList() {
        return AppManager.getActivityList();
    }

    /**
     * App处于前后台状态变化回调
     */
    public interface OnAppStatusListener {
        void onAppStatus(boolean isForeground);
    }


    //------------------------------------------内部方法---------------------------------------------//

    // App管理类
    private static class AppManager {
        private static Application application;
        private static int foreCount;// 前台Activity数量
        private static int configCount;// 正在执行changingConfigurations的Activity数量
        private static boolean isForeground = true;// 应用是否在前台
        private static final List<OnAppStatusListener> listeners = new ArrayList<>();
        private static final LinkedList<Activity> activityList = new LinkedList<>();

        // 初始化
        private static void init(Application app) {
            if (application == null) {
                application = app;
                application.registerActivityLifecycleCallbacks(new AppStatus());
            }
        }

        // 检查是否初始化
        private static void checkIsInit() {
            if (application != null) {
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

        // 获取Application
        private static Application getApp() {
            checkIsInit();
            return application;
        }

        // App是否处于前台
        private static boolean isForeground() {
            checkIsInit();
            return isForeground;
        }

        // 添加App状态变化监听
        private static void addStatusListener(OnAppStatusListener listener) {
            checkIsInit();
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        // 移除App状态变化监听
        private static void removeStatusListener(OnAppStatusListener listener) {
            checkIsInit();
            listeners.remove(listener);
        }

        // 获取Activity队列
        private static LinkedList<Activity> getActivityList() {
            checkIsInit();
            return activityList;
        }

        // 前台Activity数量增加
        private static void upForeActivityCount() {
            if (configCount < 0) {
                configCount++;
            } else {
                foreCount++;
            }
            if (foreCount >= 1 && !isForeground) {
                isForeground = true;
                for (OnAppStatusListener listener : listeners) {
                    listener.onAppStatus(isForeground);
                }
            }
        }

        // 前台Activity数量减少
        private static void downForeActivityCount(Activity activity) {
            if (activity.isChangingConfigurations()) {
                configCount--;
            } else {
                foreCount--;
            }
            if (foreCount <= 0 && isForeground) {
                isForeground = false;
                for (OnAppStatusListener listener : listeners) {
                    listener.onAppStatus(isForeground);
                }
            }
            // 前台Activity数量校准，当AppUtil.init()方法没有在Application初始化时调用，可能会出现小于0的情况
            if (foreCount < 0) {
                foreCount = 0;
            }
        }

        // 添加Activity到队列最顶部
        private static void addTopActivity(Activity activity) {
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
        private static void removeActivity(Activity activity) {
            activityList.remove(activity);
        }
    }

    // Activity生命周期回调
    private static class AppStatus implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.addTopActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            AppManager.addTopActivity(activity);
            AppManager.upForeActivityCount();
        }

        @Override
        public void onActivityResumed(Activity activity) {
            AppManager.addTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            AppManager.downForeActivityCount(activity);
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
