package com.leichao.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Mqtt消息监听器
 * Created by leichao on 2017/8/2.
 */

public interface MqttListener {

    void onConnected(boolean isConnected);
    void onReceiveMessage(MqttMessage message);
    void onSendMessage(MqttMessage message);
    void onSendSuccess(MqttMessage message);
    void onSendFailed(MqttMessage message);

}
