package com.leichao.mqtt;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Mqtt核心实现类
 * Created by leichao on 2017/8/2.
 */

public class MqttCore {

    private static volatile MqttCore instance;
    private MqttAndroidClient client;
    private Map<String, Integer> topicMap;
    private String userName;
    private String password;

    private MqttCore() {
        topicMap = new HashMap<>();
    }

    // 缺省访问权限，只能在本包内访问
    static MqttCore getInstance() {
        if (instance == null) {
            synchronized (MqttCore.class) {
                if (instance == null) {
                    instance = new MqttCore();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化MQTT
     */
    public void init(Context context, String serverUri, String clientId,
                     String userName, String password, String[] topic, int[] qos) {
        this.userName = userName;
        this.password = password;
        // 先断开连接
        disconnect();
        // 添加订阅主题
        addTopic(topic, qos);
        // 初始化Client
        client = new MqttAndroidClient(context.getApplicationContext(), serverUri, clientId);
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                // 连接成功，reconnect为false代表第一次连接成功，true代表重连成功
                MqttUtil.log("connectComplete:" + (reconnect ? "reconnect" : "connect") );
                MqttManager.getInstance().connected(true);
                subscribe();
            }

            @Override
            public void connectionLost(Throwable cause) {
                // 连接断开
                MqttUtil.log("connectionLost:" + cause);
                MqttManager.getInstance().connected(false);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // 收到消息：new String(message.getPayload())
                MqttUtil.log("messageArrived:" + new String(message.getPayload()));
                MqttManager.getInstance().receiveMessage(message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // 消息投递成功,token为publish消息之后的返回值
                try {
                    MqttMessage message = token.getMessage();
                    MqttUtil.log("deliveryComplete:" + new String(message.getPayload()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 连接服务器
     */
    public void connect() {
        if (client == null) {
            return;
        }
        try {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);// true未连接会自动重连
            mqttConnectOptions.setCleanSession(false);// false代表不清除session,可以收到上次连接订阅但发送失败的消息
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            mqttConnectOptions.setKeepAliveInterval(30);// 设置心跳时间30秒,默认为60秒
            client.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttUtil.log("connect:onSuccess");
                    try {
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        client.setBufferOpts(disconnectedBufferOptions);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MqttUtil.log("connect:onFailure:" + exception);
                }
            });
        } catch (Exception e){
            MqttUtil.log("connect:MqttException:" + e);
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (client != null) {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否连接
     */
    public boolean isConnected() {
        if (client != null) {
            try {
                return client.isConnected();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 添加主题
     */
    private void addTopic(String[] topic, int[] qos) {
        for (int i = 0; i < topic.length; i++) {
            topicMap.put(topic[i], qos[i]);
        }
    }

    /**
     * 移除主题
     */
    private void removeTopic(String[] topic) {
        for (String t : topic) {
            topicMap.remove(t);
        }
    }

    /**
     * 订阅topicMap中的主题
     */
    private void subscribe() {
        if (topicMap.size() > 0) {
            String[] topic = topicMap.keySet().toArray(new String[topicMap.size()]);
            int[] qos = new int[topic.length];
            for (int i = 0; i < topic.length; i++) {
                qos[i] = topicMap.get(topic[i]);
            }
            subscribe(topic, qos);
        }
    }

    /**
     * 订阅指定主题
     */
    public void subscribe(String[] topic, int[] qos) {
        addTopic(topic, qos);
        try {
            client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttUtil.log("subscribe:onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MqttUtil.log("subscribe:onFailure:" + exception);
                }
            });
        } catch (Exception e){
            MqttUtil.log("subscribe:MqttException:" + e);
        }
    }

    /**
     * 取消所有主题
     */
    public void unsubscribeAll() {
        String[] topic = topicMap.keySet().toArray(new String[topicMap.size()]);
        unsubscribe(topic);
    }

    /**
     * 取消指定主题
     */
    public void unsubscribe(String[] topic) {
        removeTopic(topic);
        try {
            client.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttUtil.log("unsubscribe:onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MqttUtil.log("unsubscribe:onFailure:" + exception);
                }
            });
        } catch (Exception e){
            MqttUtil.log("unsubscribe:MqttException:" + e);
        }
    }

    /**
     * 判断是否订阅了指定主题
     */
    public boolean isSubscribe(String topic) {
        return topicMap.containsKey(topic);
    }

    /**
     * 发布消息
     */
    public void publish(String topic, String content){
        final MqttMessage message = new MqttMessage();
        message.setPayload(content.getBytes());
        MqttManager.getInstance().sendMessage(message);
        try {
            // 如果没有连接，会放入消息队列等待
            client.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MqttUtil.log("publish:onSuccess");
                    MqttManager.getInstance().sendSuccess(message);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MqttUtil.log("publish:onFailure" + exception);
                    MqttManager.getInstance().sendFailed(message);
                }
            });
        } catch (Exception e) {
            MqttUtil.log("publish:MqttException:" + e);
        }
    }

}
