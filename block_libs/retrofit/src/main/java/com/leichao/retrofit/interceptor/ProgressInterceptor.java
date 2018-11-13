package com.leichao.retrofit.interceptor;

import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.progress.ProgressRequestBody;
import com.leichao.retrofit.progress.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 上传和下载进度拦截器
 * Created by leichao on 2017/3/3.
 */
public class ProgressInterceptor implements Interceptor {

    private ProgressListener upListener;
    private ProgressListener downListener;
 
    public ProgressInterceptor(ProgressListener upListener, ProgressListener downListener) {
        this.upListener = upListener;
        this.downListener = downListener;
    }
 
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (upListener != null && "POST".equals(request.method()) && request.body() != null) {
            request = request.newBuilder()
                    .post(new ProgressRequestBody(request.body(), upListener))
                    .build();
        }
        Response response = chain.proceed(request);
        if (downListener != null) {
            response = response.newBuilder()
                    .body(new ProgressResponseBody(response.body(), downListener))
                    .build();
        }
        return response;
    }

}
