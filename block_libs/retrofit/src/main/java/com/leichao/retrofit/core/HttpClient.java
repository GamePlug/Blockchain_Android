package com.leichao.retrofit.core;

import android.text.TextUtils;

import com.leichao.retrofit.Http;
import com.leichao.retrofit.converter.ConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpClient {

    private long mTimeout;
    private String mBaseUrl;
    private final List<Interceptor> interceptorList = new ArrayList<>();

    public static HttpClient builder() {
        return new HttpClient();
    }

    /**
     * 设置超时时间，单位秒
     */
    public HttpClient timeout(long timeout) {
        if (timeout > 0) this.mTimeout = timeout;
        return this;
    }

    /**
     * 设置baseUrl
     */
    public HttpClient baseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) this.mBaseUrl = baseUrl;
        return this;
    }

    /**
     * 添加拦截器
     */
    public HttpClient addInterceptor(Interceptor interceptor) {
        if (interceptor != null) this.interceptorList.add(interceptor);
        return this;
    }

    public Retrofit build() {
        HttpConfig config = Http.config();
        if (mTimeout == 0) {
            mTimeout = config.getTimeout();
        }
        if (TextUtils.isEmpty(mBaseUrl)) {
            mBaseUrl = config.getBaseUrl();
        }
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(mTimeout, TimeUnit.SECONDS)
                .writeTimeout(mTimeout, TimeUnit.SECONDS)
                .readTimeout(mTimeout, TimeUnit.SECONDS);
        for (Interceptor interceptor : interceptorList) {
            okHttpBuilder.addInterceptor(interceptor);
        }
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(ConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpBuilder.build())
                .build();
    }

}
