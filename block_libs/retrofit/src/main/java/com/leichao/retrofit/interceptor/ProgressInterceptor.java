package com.leichao.retrofit.interceptor;

import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.progress.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 下载进度拦截器
 */
public class ProgressInterceptor implements Interceptor {
 
    private ProgressListener listener;
 
    public ProgressInterceptor(ProgressListener listener) {
        this.listener = listener;
    }
 
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), listener))
                .build();
    }

}
