package com.leichao.retrofit.core;

import android.text.TextUtils;

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

    private HttpConfig mConfig;
    private long mTimeout;
    private String mBaseUrl;
    private ProgressListener mUpListener;// 上传进度监听
    private ProgressListener mDownListener;// 下载进度监听

    private HttpClient() {
        mConfig = HttpManager.config();
        mTimeout = mConfig.getTimeout();
        mBaseUrl = mConfig.getBaseUrl();
    }

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
     * 上传进度监听,POST请求才生效
     *
     * @param listener 进度监听器
     */
    public HttpClient upListener(ProgressListener listener) {
        this.mUpListener = listener;
        return this;
    }

    /**
     * 下载进度监听
     *
     * @param listener 进度监听器
     */
    public HttpClient downListener(ProgressListener listener) {
        this.mDownListener = listener;
        return this;
    }

    public Retrofit build() {
        if (mTimeout == mConfig.getTimeout()
                && mBaseUrl.equals(mConfig.getBaseUrl())
                && mUpListener == null
                && mDownListener == null) {
            if (mRetrofit == null) {
                mRetrofit = getRetrofit();
            }
            return mRetrofit;
        }
        return getRetrofit();
    }

    private Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)// 连接失败重连，默认为true，可以不加
                .connectTimeout(mTimeout, TimeUnit.SECONDS)// 链接超时
                .writeTimeout(mTimeout, TimeUnit.SECONDS)// 写入超时
                .readTimeout(mTimeout, TimeUnit.SECONDS)// 读取超时
                //.cookieJar(new CookiesManager())// 开启cookie功能，将cookie序列化到本地，暂不开启
                //.sslSocketFactory(new SSLSocketFactory())// 支持https协议，暂时未支持
                //.cache(cache)// 设置OkHttp缓存
                .addInterceptor(new ParamsInterceptor())// 添加自定义拦截操作//HttpLoggingInterceptor
                .build();
        if (mUpListener != null || mDownListener != null) {
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(new ProgressInterceptor(mUpListener, mDownListener))
                    .build();
        }
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(ConverterFactory.create())// Gson解析转换工厂//GsonConverterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// RxJava适配器
                .client(okHttpClient)
                .build();
    }

}
