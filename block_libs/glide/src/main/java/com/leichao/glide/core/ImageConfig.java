package com.leichao.glide.core;

import android.os.Environment;
import android.text.TextUtils;

/**
 * Glide配置类
 * Created by leichao on 2017/4/15.
 */

public class ImageConfig {

    // 日志打印开关，true打印，false不打印
    private boolean debug = true;

    // 超时时间，单位秒
    private long timeout = 30;

    // 全局域名
    private String baseUrl = "http://47.74.159.3:8084/";

    // 缓存地址
    private String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/image";

    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置日志打印开关
     */
    public ImageConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间，单位秒
     */
    public ImageConfig setTimeout(long timeout) {
        if (timeout > 0) this.timeout = timeout;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置baseUrl
     */
    public ImageConfig setBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) this.baseUrl = baseUrl;
        return this;
    }

    public String getCachePath() {
        return cachePath;
    }

    /**
     * 设置缓存地址
     */
    public ImageConfig setCachePath(String cachePath) {
        if (!TextUtils.isEmpty(cachePath)) this.cachePath = cachePath;
        return this;
    }
}
