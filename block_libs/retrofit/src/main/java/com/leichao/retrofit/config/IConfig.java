package com.leichao.retrofit.config;

import android.app.Dialog;
import android.content.Context;

import java.util.Map;

public interface IConfig {

    /**
     * 设置日志打印开关
     *
     * @return true打印，false不打印
     */
    boolean isDebug();

    /**
     * 设置超时时间，单位秒
     *
     * @return 超时时间
     */
    long getTimeout();

    /**
     * 设置全局域名
     *
     * @return 全局域名
     */
    String getBaseUrl();

    /**
     * 添加公共参数
     *
     * @param url 访问链接，包含了所有的键值对参数
     * @return 公共的参数
     */
    Map<String, String> getCommonParams(String url);

    /**
     * 生成加载动画Dialog
     *
     * @param context 上下文
     * @param message 加载信息提示
     * @param cancelable 是否可以返回键取消
     * @return 加载动画Dialog
     */
    Dialog getLoading(Context context, String message, boolean cancelable);

}
