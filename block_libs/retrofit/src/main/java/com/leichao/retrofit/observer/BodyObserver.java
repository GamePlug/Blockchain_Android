package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.loading.BaseLoading;

import okhttp3.ResponseBody;

public abstract class BodyObserver extends BaseObserver<ResponseBody> {

    private BaseLoading mLoading;

    public BodyObserver() {
        this(null, null, true);
    }

    public BodyObserver(Context context) {
        this(context, null, true);
    }

    public BodyObserver(Context context, String message) {
        this(context, message, true);
    }

    public BodyObserver(Context context, String message, boolean cancelable) {
        if (context != null) {
            mLoading = HttpManager.config().getCallback().getLoading(context, message, cancelable);
        }
    }

    @Override
    protected BaseLoading onHttpLoading() {
        return mLoading;
    }

    @Override
    protected final void handHttpSuccess(ResponseBody result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        httpFailure(throwable);
    }

    @Override
    protected final void handHttpCompleted() {
        httpCompleted();
    }

    private void httpSuccess(ResponseBody result) {
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

    protected abstract void onHttpSuccess(ResponseBody body);

    protected void onHttpFailure(Throwable throwable) {
    }

    protected void onHttpCompleted() {
    }
}
