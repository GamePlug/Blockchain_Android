package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.config.Config;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.util.LogUtil;

public abstract class JsonObserver<T> extends BaseObserver<T> {

    private BaseLoading mLoading;

    public JsonObserver() {
        this(null, null, true);
    }

    public JsonObserver(Context context) {
        this(context, null, true);
    }

    public JsonObserver(Context context, String message) {
        this(context, message, true);
    }

    public JsonObserver(Context context, String message, boolean cancelable) {
        if (context != null) {
            mLoading = Config.getInstance().getLoading(context, message, cancelable);
        }
    }

    @Override
    protected BaseLoading onHttpLoading() {
        return mLoading;
    }

    @Override
    protected final void handHttpSuccess(T result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        LogUtil.logE("result:" + throwable.toString());
        httpFailure(throwable);
    }

    @Override
    protected final void handHttpCompleted() {
        httpCompleted();
    }

    private void httpSuccess(T result) {
        try {
            onHttpSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpFailure(Throwable throwable) {
        try {
            onHttpFailure(throwable);
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

    protected abstract void onHttpSuccess(T result);

    protected void onHttpFailure(Throwable throwable) {
    }

    protected void onHttpCompleted() {
    }
}
