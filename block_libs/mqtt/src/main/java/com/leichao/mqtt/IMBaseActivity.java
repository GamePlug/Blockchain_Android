package com.leichao.mqtt;

import android.app.Activity;
import android.os.Bundle;

/**
 * 聊天基础Activity，无界面，只包含消息发送和接收逻辑
 * Created by leichao on 2017/7/11.
 */

public abstract class IMBaseActivity extends Activity implements MqttListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MqttManager.getInstance().addListener(this);
    }

    @Override
    protected void onDestroy() {
        MqttManager.getInstance().removeListener(this);
        super.onDestroy();
    }

    /**
     * 发送消息
     */
    public void publish(String topic, String content) {
        MqttCore.getInstance().publish(topic, content);
    }

}
