package com.leichao.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Mqtt管理类
 * Created by leichao on 2017/8/2.
 */

public class MqttManager {

    private static volatile MqttManager instance;
    private MqttListener globalListener;
    private List<MqttListener> listeners;

    private MqttManager() {
        listeners = new ArrayList<>();
    }

    // 缺省访问权限，只能在本包内访问
    static MqttManager getInstance() {
        if (instance == null) {
            synchronized (MqttManager.class) {
                if (instance == null) {
                    instance = new MqttManager();
                }
            }
        }
        return instance;
    }

    /**
     * 设置全局监听器
     */
    public void setGlobalListener(MqttListener listener) {
        this.globalListener = listener;
    }

    /**
     * 添加普通监听器
     */
    public void addListener(MqttListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移除普通监听器
     */
    public void removeListener(MqttListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * 通知连接状态变化
     */
    public void connected(boolean isConnected) {
        if (globalListener != null) {
            globalListener.onConnected(isConnected);
        }
        for (MqttListener listener : listeners) {
            listener.onConnected(isConnected);
        }
    }

    /**
     * 收到聊天消息
     */
    public void receiveMessage(MqttMessage message) {
        if (globalListener != null) {
            globalListener.onReceiveMessage(message);
        }
        for (MqttListener listener : listeners) {
            listener.onReceiveMessage(message);
        }
    }

    /**
     * 发送聊天消息
     */
    public void sendMessage(MqttMessage message) {
        if (globalListener != null) {
            globalListener.onSendMessage(message);
        }
        for (MqttListener listener : listeners) {
            listener.onSendMessage(message);
        }
    }

    /**
     * 发送消息成功
     */
    public void sendSuccess(MqttMessage message) {
        if (globalListener != null) {
            globalListener.onSendSuccess(message);
        }
        for (MqttListener listener : listeners) {
            listener.onSendSuccess(message);
        }
    }

    /**
     * 发送消息失败
     */
    public void sendFailed(MqttMessage message) {
        if (globalListener != null) {
            globalListener.onSendFailed(message);
        }
        for (MqttListener listener : listeners) {
            listener.onSendFailed(message);
        }
    }

}
