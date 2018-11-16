package com.leichao.retrofit.interceptor;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Map;

import okhttp3.Headers;

/**
 * 默认参数拦截器
 * Created by leichao on 2017/3/3.
 */
public class DefaultParamsInterceptor extends ParamsInterceptor {

    @NonNull
    @Override
    public Map<String, String> getCommonHeaders(Headers headers) {
        return Collections.emptyMap();
    }

    @NonNull
    @Override
    public Map<String, String> getCommonParams(String url) {
        return Collections.emptyMap();
    }

}
