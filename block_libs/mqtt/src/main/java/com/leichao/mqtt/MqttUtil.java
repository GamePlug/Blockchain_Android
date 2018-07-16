package com.leichao.mqtt;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * IM工具类
 * Created by leichao on 2017/7/7.
 */

public class MqttUtil {

    public static final String TAG = "MQTT";// 日志打印TAG

    static boolean DEBUG = true;

    public static void log(String log) {
        if (DEBUG) {
            Log.i(TAG, log);
        }
    }

    public static String getString(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
        }
        return "";
    }

}
