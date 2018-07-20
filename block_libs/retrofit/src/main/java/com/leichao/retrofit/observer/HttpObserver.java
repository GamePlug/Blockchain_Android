package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.util.LogUtil;

import java.io.IOException;

import retrofit2.HttpException;

public abstract class HttpObserver<T> extends BaseObserver<HttpResult<T>> {

    public HttpObserver() {
    }

    public HttpObserver(Context context) {
        super(context);
    }

    public HttpObserver(Context context, String message) {
        super(context, message);
    }

    public HttpObserver(Context context, String message, boolean cancelable) {
        super(context, message, cancelable);
    }

    @Override
    protected final void handHttpSuccess(HttpResult<T> result) {
        if (HttpResult.Status.ERROR_JSON == result.getStatus()) {
            httpFailure(result);
        } else if ("success".equals(result.getCode())) {
            result.setStatus(HttpResult.Status.TYPE_TRUE);
            httpSuccess(result);
        } else {
            result.setStatus(HttpResult.Status.TYPE_FALSE);
            // 统一处理登录时效，登录冲突等情况
            if ("-2".equals(result.getCode())) {
                // 登陆冲突
            } else {
                httpFailure(result);
            }
        }
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        LogUtil.logE("result:" + throwable.toString());
        HttpResult<T> result = new HttpResult<>();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            result.setCode(String.valueOf(httpException.code()));
            if (code == 504) {
                // 网络异常，且开启了缓存但获取缓存失败时。
                result.setStatus(HttpResult.Status.ERROR_NET);
                result.setMessage("网络不给力");
            } else {
                // 服务器端程序抛出异常。
                result.setStatus(HttpResult.Status.ERROR_OTHER);
                result.setMessage("网络连接异常");
                if (code == 500) {
                    //uploadErrorToServer(httpException.response().errorBody());
                }
            }
        } else if (throwable instanceof IOException) {// ConnectException,SocketTimeoutException,UnknownHostException
            // 网络异常
            result.setStatus(HttpResult.Status.ERROR_NET);
            result.setMessage("网络不给力");
        } else {
            // 客户端程序异常，一般是开发者在回调方法中发生了异常
            throwable.printStackTrace();
            result.setStatus(HttpResult.Status.ERROR_OTHER);
            result.setMessage("网络连接异常");
        }
        httpFailure(result);
    }

    @Override
    protected final void handHttpCompleted() {
        httpCompleted();
    }

    private void httpSuccess(HttpResult<T> result) {
        try {
            onHttpSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpFailure(HttpResult<T> result) {
        try {
            onHttpFailure(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpCompleted() {
        try {
            onHttpCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onHttpSuccess(HttpResult<T> result);

    protected void onHttpFailure(HttpResult<T> result) {
    }

    protected void onHttpCompleted() {
    }
}