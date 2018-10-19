package com.leichao.retrofit.config;

import android.app.Dialog;
import android.content.Context;

import com.leichao.retrofit.loading.DefaultLoading;

import java.util.Collections;
import java.util.Map;

public class ConfigImpl implements IConfig {

    public boolean isDebug() {
        return true;
    }

    @Override
    public long getTimeout() {
        return 30;
    }

    public String getBaseUrl() {
        return "http://47.74.159.3:8084/";
    }

    public Map<String, String> getCommonParams(String url) {
        return Collections.emptyMap();
    }

    @Override
    public Dialog getLoading(Context context, String message, boolean cancelable) {
        return new DefaultLoading(context, message, cancelable);
    }

}
