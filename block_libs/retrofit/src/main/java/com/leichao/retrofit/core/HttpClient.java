package com.leichao.retrofit.core;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.converter.ConverterFactory;
import com.leichao.retrofit.interceptor.ParamsInterceptor;
import com.leichao.retrofit.interceptor.ProgressInterceptor;
import com.leichao.retrofit.progress.ProgressListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpClient {

    private static Retrofit mRetrofit;

    public static Retrofit getRetrofit() {
        return getRetrofit(null);
    }

    public static Retrofit getRetrofit(ProgressListener listener) {
        if (listener != null) {
            return createRetrofit(listener);
        } else {
            if (mRetrofit == null) {
                mRetrofit = createRetrofit(null);
            }
            return mRetrofit;
        }
    }

    private static Retrofit createRetrofit(ProgressListener listener) {
        long timeout = HttpManager.config().getTimeout();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)// 连接失败重连，默认为true，可以不加
                .connectTimeout(timeout, TimeUnit.SECONDS)// 链接超时
                .writeTimeout(timeout, TimeUnit.SECONDS)// 写入超时
                .readTimeout(timeout, TimeUnit.SECONDS)// 读取超时
                //.cookieJar(new CookiesManager())// 开启cookie功能，将cookie序列化到本地，暂不开启
                //.sslSocketFactory(new SSLSocketFactory())// 支持https协议，暂时未支持
                //.cache(cache)// 设置OkHttp缓存
                .addInterceptor(new ParamsInterceptor())// 添加自定义拦截操作//HttpLoggingInterceptor
                .build();
        if (listener != null) {
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(new ProgressInterceptor(listener))
                    .build();
        }
        return new Retrofit.Builder()
                .baseUrl(HttpManager.config().getBaseUrl())
                .addConverterFactory(ConverterFactory.create())// Gson解析转换工厂//GsonConverterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// RxJava适配器
                .client(okHttpClient)
                .build();
    }

}
