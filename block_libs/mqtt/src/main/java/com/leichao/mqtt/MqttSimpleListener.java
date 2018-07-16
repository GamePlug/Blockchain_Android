package com.leichao.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Mqtt消息监听器
 * Created by leichao on 2017/9/8.
 */

public abstract class MqttSimpleListener implements MqttListener {
    @Override
    public void onConnected(boolean isConnected) {

    }

    @Override
    public void onReceiveMessage(MqttMessage message) {

    }

    @Override
    public void onSendMessage(MqttMessage message) {

    }

    @Override
    public void onSendSuccess(MqttMessage message) {

    }

    @Override
    public void onSendFailed(MqttMessage message) {

    }
}
