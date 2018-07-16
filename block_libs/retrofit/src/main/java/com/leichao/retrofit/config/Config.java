package com.leichao.retrofit.config;

import android.app.Dialog;
import android.content.Context;

import java.util.Map;

/**
 * Retrofit配置类
 * Created by leichao on 2017/4/15.
 */

public class Config {

    private static volatile Config instance;
    private IConfig mConfig;
    private IConfig mDefaultConfig;

    private Config() {
        mDefaultConfig = new ConfigImpl();
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    /**
     * 设置配置
     */
    public void setConfig(IConfig config) {
        this.mConfig = config;
    }

    /**
     * 是否打印日志
     */
    public boolean isDebug() {
        if (mConfig != null) {
            return mConfig.isDebug();
        }
        return mDefaultConfig.isDebug();
    }

    /**
     * 获取超时时间
     */
    public long getTimeout() {
        if (mConfig != null) {
            long timeout = mConfig.getTimeout();
            if (timeout > 0) {
                return timeout;
            }
        }
        return mDefaultConfig.getTimeout();
    }

    /**
     * 获取全局域名
     */
    public String getBaseUrl() {
        if (mConfig != null) {
            String baseUrl = mConfig.getBaseUrl();
            if (baseUrl != null) {
                return baseUrl;
            }
        }
        return mDefaultConfig.getBaseUrl();
    }

    /**
     * 获取公共参数
     */
    public Map<String, String> getCommonParams(String url) {
        if (mConfig != null) {
            Map<String, String> commonParams = mConfig.getCommonParams(url);
            if (commonParams != null) {
                return commonParams;
            }
        }
        return mDefaultConfig.getCommonParams(url);
    }

    /**
     * 获取加载动画
     */
    public Dialog getLoading(Context context, String message, boolean cancelable) {
        if (mConfig != null) {
            Dialog loading = mConfig.getLoading(context, message, cancelable);
            if (loading != null) {
                return loading;
            }
        }
        return mDefaultConfig.getLoading(context, message, cancelable);
    }

}
