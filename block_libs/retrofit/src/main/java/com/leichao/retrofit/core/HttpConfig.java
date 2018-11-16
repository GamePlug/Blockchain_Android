package com.leichao.retrofit.core;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.leichao.retrofit.interceptor.DefaultParamsInterceptor;
import com.leichao.retrofit.interceptor.ParamsInterceptor;
import com.leichao.retrofit.loading.BaseLoading;

import okhttp3.CookieJar;

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

    // cookie管理器
    private CookieJar cookieJar = CookieJar.NO_COOKIES;

    // 参数处理，可以设置为空
    private ParamsInterceptor paramsInterceptor = new DefaultParamsInterceptor();

    // 下载缓存地址
    private String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/download";

    // 加载loading回调
    private LoadingCallback loadingCallback = new LoadingCallback();

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
     * 设置baseUrl
     */
    public HttpConfig setBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) this.baseUrl = baseUrl;
        return this;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    /**
     * 设置cookie管理器
     */
    public HttpConfig setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public ParamsInterceptor getParamsInterceptor() {
        return paramsInterceptor;
    }

    /**
     * 参数处理器，可以设置为空
     */
    public HttpConfig setParamsInterceptor(ParamsInterceptor paramsInterceptor) {
        this.paramsInterceptor = paramsInterceptor;
        return this;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    /**
     * 设置缓存地址
     */
    public HttpConfig setDownloadPath(String downloadPath) {
        if (!TextUtils.isEmpty(downloadPath)) this.downloadPath = downloadPath;
        return this;
    }

    public LoadingCallback getLoadingCallback() {
        return loadingCallback;
    }

    /**
     * 设置加载loading回调
     */
    public HttpConfig setLoadingCallback(LoadingCallback callback) {
        if (callback != null) this.loadingCallback = callback;
        return this;
    }

    public static class LoadingCallback {
        /**
         * 生成加载loading
         *
         * @param context 上下文
         * @param message 加载信息提示
         * @param cancelable 是否可以返回键取消
         * @return BaseLoading
         */
        public BaseLoading newLoading(Context context, String message, boolean cancelable) {
            return null;
        }
    }
}
