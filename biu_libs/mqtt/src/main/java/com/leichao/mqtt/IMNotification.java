package com.leichao.mqtt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * 推送通知栏
 * Created by leichao on 2017/3/21.
 */

public class IMNotification {

    private static IMNotification instance;
    private NotificationManager manager;
    private int mId = 0;
    private String appName;
    private String channelId;
    private String channelName;
    private int appIcon;

    private IMNotification(Context context) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            appName = info.loadLabel(pm).toString();
            appIcon = info.icon;
            channelId = appName;
            channelName = channelId;
            //适配安卓8.0的消息渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 缺省访问权限，只能在本包内访问
    static IMNotification getInstance(Context context) {
        if (instance == null) {
            instance = new IMNotification(context);
        }
        return instance;
    }

    public int show(Context context, Intent intent, String title, String content) {
        int id = nextId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return show(context, pendingIntent, id, title, content);
    }

    public int show(Context context, PendingIntent pendingIntent, int id, String title, String content) {
        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(title)) {
            // 如果标题和内容都为空，则直接返回，不显示
            return id;
        } else if (TextUtils.isEmpty(content)) {
            // 如果内容为空，则内容显示为标题，标题显示为应用名，
            content = title;
            title = appName;
        } else if (TextUtils.isEmpty(title)) {
            // 如果标题为空，则标题显示应用名
            title = appName;
        }
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context,channelId);
        }else{
            builder = new Notification.Builder(context);
        }
        builder.setContentTitle(title)//设置下拉列表里的标题
                .setContentText(content)//设置上下文内容
                .setTicker(content)//首次提醒会有动画上升效果
                .setSmallIcon(appIcon)//设置小图标
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), appIcon))//设置大图标
                .setWhen(System.currentTimeMillis())//设置事件发生时间
                .setAutoCancel(true) //自动取消
                .setContentIntent(pendingIntent)//发送广播
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);//声音和震动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setShowWhen(true);//SDK17及以上需要setShowWhen才能显示时间
        }
        Notification notification = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ?
                builder.build() : builder.getNotification();//SDK16及以上才能使用build方法
        //notification.flags |= Notification.FLAG_ONGOING_EVENT;//不让侧滑删除
        manager.notify(id, notification);
        return id;
    }

    public void cancel(int id) {
        manager.cancel(id);
    }

    public void cancelAll() {
        manager.cancelAll();
    }

    public int nextId() {
        return ++mId;
    }
}
