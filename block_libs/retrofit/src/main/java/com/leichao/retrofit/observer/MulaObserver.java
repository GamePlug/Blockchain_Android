package com.leichao.retrofit.observer;

import com.google.gson.JsonParseException;
import com.leichao.retrofit.result.MulaResult;

import java.io.IOException;

import retrofit2.HttpException;

public abstract class MulaObserver<T> extends BaseObserver<MulaResult<T>> {

    @Override
    protected final void handHttpSuccess(MulaResult<T> result) {
        if ("success".equals(result.getCode())) {
            result.setStatus(MulaResult.Status.CODE_SUCCESS);
            httpSuccess(result);
        } else {
            result.setStatus(MulaResult.Status.CODE_FAILURE);
            // 统一处理登录失效，登录冲突等情况
            if ("-2".equals(result.getCode())) {
                // 登陆冲突
            } else {
                httpFailure(result);
            }
        }
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        MulaResult<T> result = new MulaResult<>();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            result.setCode(String.valueOf(httpException.code()));
            if (code == 504) {
                // 网络异常，且开启了缓存但获取缓存失败时。
                result.setStatus(MulaResult.Status.ERROR_NET);
                result.setMessage("网络不给力");
            } else {
                // 服务器端程序抛出异常。
                result.setStatus(MulaResult.Status.ERROR_OTHER);
                result.setMessage("网络连接异常");
                if (code == 500) {
                    //uploadErrorToServer(httpException.response().errorBody());
                }
            }
        } else if (throwable instanceof IOException) {// ConnectException,SocketTimeoutException,UnknownHostException
            // 网络异常
            result.setStatus(MulaResult.Status.ERROR_NET);
            result.setMessage("网络不给力");
        } else if (throwable instanceof JsonParseException) {
            // Json解析异常
            result.setStatus(MulaResult.Status.ERROR_JSON);
            result.setMessage("Json解析异常");
        } else {
            // 客户端程序异常，一般是开发者在回调方法中发生了异常
            throwable.printStackTrace();
            result.setStatus(MulaResult.Status.ERROR_OTHER);
            result.setMessage("网络连接异常");
        }
        httpFailure(result);
    }

    private void httpSuccess(MulaResult<T> result) {
        try {
            onHttpSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpFailure(MulaResult<T> result) {
        try {
            onHttpFailure(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onHttpSuccess(MulaResult<T> result);

    protected void onHttpFailure(MulaResult<T> result) {
    }

}
