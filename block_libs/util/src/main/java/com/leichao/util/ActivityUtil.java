package com.leichao.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class ActivityUtil {

    /**
     * 获取Activity队列
     *
     * @return LinkedList<Activity>
     */
    public static LinkedList<Activity> getActivityList() {
        return AppUtil.getActivityList();
    }

    /**
     * 获取最上层的Activity,若没有则返回null
     *
     * @return Activity
     */
    public static Activity getTopActivity() {
        LinkedList<Activity> activityList = getActivityList();
        if (!activityList.isEmpty()) {
            final Activity topActivity = activityList.getLast();
            if (topActivity != null) {
                return topActivity;
            }
        }
        return null;
    }

    /**
     * Finish所有Activity
     */
    public static void finishAllActivity() {
        List<Activity> activityList = getActivityList();
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            Activity activity = activityList.get(i);
            activity.finish();
        }
    }

    /**
     * Finish到指定的Activity.
     *
     * @param activity Activity
     * @param isIncludeSelf true结束指定的Activity自身,false不结束
     *
     * @return true成功,false失败
     */
    public static boolean finishToActivity(Activity activity, boolean isIncludeSelf) {
        List<Activity> activityList = getActivityList();
        if (activityList.contains(activity)) {
            for (int i = activityList.size() - 1; i >= 0; --i) {
                Activity aActivity = activityList.get(i);
                if (aActivity.equals(activity)) {
                    if (isIncludeSelf) {
                        aActivity.finish();
                    }
                    return true;
                }
                aActivity.finish();
            }
        }
        return false;
    }

    /**
     * Finish到指定的Activity.
     *
     * @param clz Activity class
     * @param isIncludeSelf true结束指定的Activity自身,false不结束
     *
     * @return true成功,false失败
     */
    public static boolean finishToActivity(Class<? extends Activity> clz, boolean isIncludeSelf) {
        List<Activity> activityList = getActivityList();
        boolean contains = false;
        for (Activity aActivity : activityList) {
            if (aActivity.getClass().equals(clz)) {
                contains = true;
            }
        }
        if (contains) {
            for (int i = activityList.size() - 1; i >= 0; --i) {
                Activity aActivity = activityList.get(i);
                if (aActivity.getClass().equals(clz)) {
                    if (isIncludeSelf) {
                        aActivity.finish();
                    }
                    return true;
                }
                aActivity.finish();
            }
        }
        return false;
    }

}
