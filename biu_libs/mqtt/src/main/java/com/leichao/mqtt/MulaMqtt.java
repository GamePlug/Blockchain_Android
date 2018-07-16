package com.leichao.mqtt;

import android.content.Context;
import android.content.Intent;

/**
 * Mqtt对外开放类
 * Created by leichao on 2017/8/14.
 */

public class MulaMqtt {

    //public static final String SERVER_URI = "tcp://www.mulacar.cn:1883";// 默认服务器地址
    public static String USERNAME = "admin";// 默认用户名
    public static String PASSWORD = "admin";// 默认密码

    /**
     * 初始化
     */
    public static void init(Context context, String serverUri, String clientId) {
        MqttCore.getInstance().init(context, serverUri, clientId, USERNAME, PASSWORD, new String[0], new int[0]);
    }

    /**
     * 初始化，主题的订阅级别默认为2
     */
    public static void init(Context context, String serverUri, String clientId, String[] topic) {
        int[] qos = new int[topic.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = 2;
        }
        MqttCore.getInstance().init(context, serverUri, clientId, USERNAME, PASSWORD, topic, qos);
    }

    /**
     * 连接服务器
     */
    public static void connect() {
        MqttCore.getInstance().connect();
    }

    /**
     * 断开连接
     */
    public static void disconnect() {
        MqttCore.getInstance().disconnect();
    }

    /**
     * 是否连接
     */
    public static boolean isConnected() {
        return MqttCore.getInstance().isConnected();
    }

    /**
     * 订阅指定主题
     */
    public static void subscribe(String[] topic) {
        int[] qos = new int[topic.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = 2;
        }
        MqttCore.getInstance().subscribe(topic, qos);
    }

    /**
     * 取消指定主题
     */
    public static void unsubscribe(String[] topic) {
        MqttCore.getInstance().unsubscribe(topic);
    }

    /**
     * 取消所有主题
     */
    public static void unsubscribeAll() {
        MqttCore.getInstance().unsubscribeAll();
    }

    /**
     * 判断是否订阅了指定主题
     */
    public static boolean isSubscribe(String topic) {
        return MqttCore.getInstance().isSubscribe(topic);
    }

    /**
     * 发布消息
     */
    public static void publish(String topic, String content) {
        MqttCore.getInstance().publish(topic, content);
    }

    /**
     * 设置是否打印日志
     */
    public static void setDebug(boolean debug) {
        MqttUtil.DEBUG = debug;
    }

    /**
     * 设置全局监听器
     */
    public static void setGlobalListener(MqttListener listener) {
        MqttManager.getInstance().setGlobalListener(listener);
    }

    /**
     * 添加普通监听器
     */
    public static void addListener(MqttListener listener) {
        MqttManager.getInstance().addListener(listener);
    }

    /**
     * 移除普通监听器
     */
    public static void removeListener(MqttListener listener) {
        MqttManager.getInstance().removeListener(listener);
    }

    /**
     * 显示通知栏
     * @return 通知栏id
     */
    public static int showNotification(Context context, Intent intent, String title, String content) {
        return IMNotification.getInstance(context).show(context, intent, title, content);
    }

    /**
     * 取消指定id的通知栏显示
     */
    public static void cancelNotification(Context context, int id) {
        IMNotification.getInstance(context).cancel(id);
    }

    /**
     * 取消所有的通知栏显示
     */
    public static void cancelAllNotification(Context context) {
        IMNotification.getInstance(context).cancelAll();
    }

}
