package com.leichao.retrofit.core;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.leichao.retrofit.loading.BaseLoading;

import java.util.Collections;
import java.util.Map;

/**
 * Retrofit配置类
 * Created by leichao on 2017/4/15.
 */

public class HttpConfig {

    // 日志打印开关，true打印，false不打印
    private boolean debug = true;

    // 超时时间，单位秒
    private long timeout = 30;

    // 全局域名
    private String baseUrl = "http://47.74.159.3:8084/";

    // 下载缓存地址
    private String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/download";

    // 动态设置回调
    private Callback callback = new Callback();

    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置日志打印开关
     */
    public HttpConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间，单位秒
     */
    public HttpConfig setTimeout(long timeout) {
        if (timeout > 0) this.timeout = timeout;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置全局域名
     */
    public HttpConfig setBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) this.baseUrl = baseUrl;
        return this;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    /**
     * 设置缓存地址
     */
    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public Callback getCallback() {
        return callback;
    }

    /**
     * 设置动态设置回调
     *
     * @param callback 动态设置回调
     */
    public HttpConfig setCallback(Callback callback) {
        if (callback != null) this.callback = callback;
        return this;
    }

    public static class Callback {
        /**
         * 添加公共参数
         *
         * @param url 访问链接，包含了所有的键值对参数
         * @return 公共的参数
         */
        public Map<String, String> getCommonParams(String url) {
            return Collections.emptyMap();
        }

        /**
         * 生成加载动画Dialog
         *
         * @param context 上下文
         * @param message 加载信息提示
         * @param cancelable 是否可以返回键取消
         * @return 加载动画Dialog
         */
        public BaseLoading getLoading(Context context, String message, boolean cancelable) {
            return null;
        }
    }
}
